package co.sptnk.service.user.services.Impl;

import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.Interest;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.repositories.InterestRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.IUserService;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    @Lazy
    @Autowired
    private RealmResource keyCloak;


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
        user.setFirstname(params.get("firstname"));
        user.setLastname(params.get("lastname"));
        user.setUsername(params.get("username"));
        user.setBirthDate(params.get("birthDate") != null ? LocalDate.parse(params.get("birthDate"), formatter):null);
        user.setEmail(params.get("email"));
        user.setBlocked(params.get("blocked") != null ? Boolean.parseBoolean(params.get("blocked")): null);
        user.setDeleted(params.get("deleted") != null ? Boolean.parseBoolean(params.get("deleted")): null);
        if (params.get("interestId") != null) {
            Interest interest = interestRepo.findInterestByIdAndDeletedFalse(Long.parseLong(params.get("interestId"))).orElse(null);
            List<Interest> list = new ArrayList<>();
            list.add(interest);
            user.setInterests(list);
        }
        return Example.of(user);
    }

    @Override
    public List<User> getAllNotDeleted() {
        return new ArrayList<>(usersRepo.findAllByDeletedFalse());
    }

    private User getUserFromKeyclock(UUID id){
        RealmResource realmResource = keyCloak;
        UserResource userResource = realmResource.users().get(id.toString());
        User user = null;
        if (userResource != null) {
            UserRepresentation userRepresentation = userResource.toRepresentation();
            user = new User(userRepresentation);
        }
        return user;
    }


    private void updateUserInKeyclock(User user){
        RealmResource realmResource = keyCloak;
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = updateUserRepresentationFromUser(userResource.toRepresentation(), user);
        userResource.update(userRepresentation);
    }

    private void blockUserInKeyclock(User user) {
        RealmResource realmResource = keyCloak;
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(false);
        userResource.update(userRepresentation);
    }

    private UserRepresentation updateUserRepresentationFromUser(UserRepresentation userRepresentation, User user) {
        if (userRepresentation != null && user != null) {
            userRepresentation.setFirstName(user.getFirstname());
            userRepresentation.setLastName(user.getLastname());
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setFirstName(user.getFirstname());
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setEnabled(!user.getBlocked());
        }
        return userRepresentation;
    }

}
