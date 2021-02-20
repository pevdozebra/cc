package co.sptnk.service.user.services.Impl;

import co.sptnk.service.user.common.PageableCreator;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.model.Interest;
import co.sptnk.service.user.model.UserDetails;
import co.sptnk.service.user.model.dto.UserSignUpData;
import co.sptnk.service.user.repositories.InterestRepo;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.services.IUserService;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private InterestRepo interestRepo;
    @Autowired
    private Environment environment;
    @Autowired
    private ModelMapper modelMapper;


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
        if (params.get("interestId") != null) {
            Interest interest = interestRepo.findInterestByIdAndDeletedFalse(Long.parseLong(params.get("interestId"))).orElse(null);
            List<Interest> list = new ArrayList<>();
            list.add(interest);
            user.getInterests().addAll(list);
        }
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

    @Override
    public AccessTokenResponse signUp(UserSignUpData userSignUpData) {
        if (usersRepo.findUserByUsername(userSignUpData.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        AccessTokenResponse accessTokenResponse;

        try {

            UserDetails userDetails = new UserDetails(userSignUpData);
            User user = new User(userSignUpData);

            RealmResource realmResource = getKeyclockRealmResourceByClient();
            UserRepresentation userRepresentation = realmResource.users().search(user.getUsername()).stream().findFirst().orElse(null);
            if  (userRepresentation !=null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            } else {
                userRepresentation = new UserRepresentation();
            }

            userRepresentation.setEnabled(true);
            userRepresentation = updateUserRepresentationFromUser(userRepresentation, user);
            Response response = realmResource.users().create(userRepresentation);
            String userId = CreatedResponseUtil.getCreatedId(response);

            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            String password = UUID.randomUUID().toString();
            passwordCred.setValue(password);

            UserResource userResource = realmResource.users().get(userId);
            userResource.resetPassword(passwordCred);
//                RoleRepresentation roleRepresentation = realmResource.roles()
//                        .get("role").toRepresentation();
//
//                userResource.roles().realmLevel()
//                        .add(Arrays.asList(roleRepresentation));

            user.setId(UUID.fromString(userId));
            userDetails.setId(UUID.fromString(userId));
            user.setUserDetails(userDetails);
            usersRepo.save(user);

            accessTokenResponse = getAccessTokenResponse(user.getUsername(), password);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return accessTokenResponse;
    }

    private User getUserFromKeyclock(UUID id){
        RealmResource realmResource = getKeyclockRealmResourceByClient();
        UserResource userResource = realmResource.users().get(id.toString());
        User user = null;
        if (userResource != null) {
            UserRepresentation userRepresentation = userResource.toRepresentation();
            user = new User(userRepresentation);
        }
        return user;
    }


    private void updateUserInKeyclock(User user){
        RealmResource realmResource = getKeyclockRealmResourceByClient();
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = updateUserRepresentationFromUser(userResource.toRepresentation(), user);
        userResource.update(userRepresentation);
    }

    private void blockUserInKeyclock(User user) {
        RealmResource realmResource = getKeyclockRealmResourceByClient();
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
            userRepresentation.setEnabled(user.getBlocked()!=null ? !user.getBlocked() : null);
        }
        return userRepresentation;
    }

    // https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547/2
    private RealmResource getKeyclockRealmResourceByClient(){
           Keycloak keycloak = KeycloakBuilder
                .builder()
                .serverUrl(environment.getProperty("keycloak.auth-server-url"))
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(environment.getProperty("keycloak.realm"))
                .clientId(environment.getProperty("keycloak.resource"))
                .clientSecret(environment.getProperty("client-secret"))
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();
        keycloak.tokenManager().getAccessToken();
        RealmResource realmResource = keycloak.realm("celebrity-chat");
        return realmResource;
    }

    private AccessTokenResponse getAccessTokenResponse(String username, String password) {
        Keycloak keycloak = KeycloakBuilder
                .builder()
                .serverUrl(environment.getProperty("keycloak.auth-server-url"))
                .grantType(OAuth2Constants.PASSWORD)
                .realm(environment.getProperty("keycloak.realm"))
                .clientId(environment.getProperty("keycloak.resource"))
                .username(username)
                .password(password)
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();
        return keycloak.tokenManager().getAccessToken();
    }

}
