package co.sptnk.service.user.services.Impl;

import co.sptnk.service.user.common.KeycloakProvider;
import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.Interest;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.model.UserDetails;
import co.sptnk.service.user.model.dto.UserSignUpData;
import co.sptnk.service.user.repositories.InterestRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.IUserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private InterestRepo interestRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KeycloakProvider keycloakProvider;


    @Override
    public User add(User user) {
//        if (user.getId() != null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//        return usersRepo.save(user);
        return null;
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setDeleted(null);
        User exist = usersRepo.findUserByIdAndDeletedFalse(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(user, exist);
        updateUserInKeyclock(exist);
        return exist;
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        User user = usersRepo.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        blockUserInKeyclock(user);
        user.setDeleted(true);
    }

    @Override
    public User getOneById(UUID uuid) {
        User user = usersRepo.findUserByIdAndDeletedFalse(uuid).orElse(null);
        if (user == null) {
            User deletedUser = usersRepo.findById(uuid).orElse(null);
            user = getUserFromKeyclock(uuid);
            if (user != null && !user.getBlocked() && deletedUser == null) {
                usersRepo.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        return user;
    }

    @Override
    public List<User> getAll(Map<String, String> params) {
        Page<User> page;
        try {
            page = usersRepo.findAll(getExample(params), PageableCreator.getPageable(params));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ArrayList<>(page.getContent());
    }

    private Example<User> getExample(Map<String, String> params){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        User user = new User();
        user.setId(params.get("id") != null ? UUID.fromString(params.get("id")):null);
        user.setFirstName(params.get("firstname"));
        user.setLastName(params.get("lastname"));
        user.setUsername(params.get("username"));
        user.setBirthDate(params.get("birthDate") != null ? LocalDate.parse(params.get("birthDate"), formatter):null);
        user.setEmail(params.get("email"));
        user.setBlocked(params.get("blocked") != null ? Boolean.parseBoolean(params.get("blocked")): null);
        user.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): null);
        return Example.of(user);
    }

    @Override
    public List<User> getAllNotDeleted() {
        return new ArrayList<>(usersRepo.findAllByDeletedFalse());
    }

    @Override
    @Transactional
    public Set<Interest> addInterests(Set<Long> ids, UUID userId) {
        User user = usersRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Interest> interests = new HashSet<>(interestRepo.findByDeletedFalseAndIdIn(ids));
        user.getInterests().addAll(interests);
        return user.getInterests();
    }

    @Override
    @Transactional
    public Set<Interest> deleteInterests(Set<Long> ids, UUID userId) {
        User user = usersRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Set<Interest> interests = new HashSet<>(interestRepo.findByDeletedFalseAndIdIn(ids));
        user.getInterests().removeAll(interests);
        return user.getInterests();
    }


    public User createUser(UserSignUpData userSignUpData){
        if (usersRepo.findUserByUsername(userSignUpData.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = new UserDetails(userSignUpData);
        User user = new User(userSignUpData);
        String userId = createKeycloakUser(user);

        user.setId(UUID.fromString(userId));
        userDetails.setId(UUID.fromString(userId));
        user.setUserDetails(userDetails);
        return usersRepo.save(user);
    }

    private String createKeycloakUser(User user) {
        String userId;
        try {
            Keycloak keycloak = keycloakProvider.get();
            RealmResource realmResource = keycloak.realm(environment.getProperty("keycloak.realm"));
            UserRepresentation userRepresentation = realmResource
                    .users()
                    .search(user.getUsername())
                    .stream()
                    .findFirst()
                    .orElse(null);

            if  (userRepresentation !=null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                userRepresentation = new UserRepresentation();
            }

            userRepresentation.setEnabled(true);
            userRepresentation = updateUserRepresentationFromUser(userRepresentation, user);
            Response response = realmResource.users().create(userRepresentation);
            userId = CreatedResponseUtil.getCreatedId(response);

//                RoleRepresentation roleRepresentation = realmResource.roles()
//                        .get("role").toRepresentation();
//
//                userResource.roles().realmLevel()
//                        .add(Arrays.asList(roleRepresentation));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return userId;
    }

    private User getUserFromKeyclock(UUID id){
        RealmResource realmResource = keycloakProvider.get().realm(environment.getProperty("keycloak.realm"));
        UserResource userResource = realmResource.users().get(id.toString());
        User user = null;
        if (userResource != null) {
            UserRepresentation userRepresentation = userResource.toRepresentation();
            user = new User(userRepresentation);
        }
        return user;
    }


    private void updateUserInKeyclock(User user){
        RealmResource realmResource = keycloakProvider.get().realm(environment.getProperty("keycloak.realm"));
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = updateUserRepresentationFromUser(userResource.toRepresentation(), user);
        userResource.update(userRepresentation);
    }

    private void blockUserInKeyclock(User user) {
        RealmResource realmResource = keycloakProvider.get().realm(environment.getProperty("keycloak.realm"));
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(false);
        userResource.update(userRepresentation);
    }

    private UserRepresentation updateUserRepresentationFromUser(UserRepresentation userRepresentation, User user) {
        if (userRepresentation != null && user != null) {
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setEnabled(user.getBlocked() == null || !user.getBlocked());
        }
        return userRepresentation;
    }
}
