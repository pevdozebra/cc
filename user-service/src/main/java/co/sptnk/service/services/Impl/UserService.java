package co.sptnk.service.services.Impl;

import co.sptnk.service.model.Card;
import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.CardsRepo;
import co.sptnk.service.repositories.PerformerRatingsRepo;
import co.sptnk.service.repositories.UsersRepo;
import co.sptnk.service.services.IUserService;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private CardsRepo cardsRepo;

    @Autowired
    private PerformerRatingsRepo performerRatingsRepo;

    @Autowired
    private Environment environment;

    @Override
    public User add(User user) {
        if (user.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return usersRepo.save(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User exist = usersRepo.findUserByIdAndDeletedFalse(user.getId()).orElse(null);
        if (exist == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        updateUserInKeyclock(user);
        return usersRepo.save(user);
    }

    @Override
    public void delete(UUID uuid) {
        User user = usersRepo.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getDeleted() != null && user.getDeleted()) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Card> cards = (List<Card>) cardsRepo.findCardByUserAndDeletedFalse(user);
        cards.forEach(card -> {
            card.setDeleted(true);
            cardsRepo.save(card);
        });
        List<PerformerRating> ratings = (List<PerformerRating>) performerRatingsRepo.findPerformerRatingByRatedAndDeletedFalse(user);
        ratings.forEach(rating -> {
            rating.setDeleted(true);
            performerRatingsRepo.save(rating);
        });

        user.getInterests().clear();
        user.setDeleted(true);
        blockUserInKeyclock(user);
        usersRepo.save(user);
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
        return usersRepo.findAll();
    }

    @Override
    public List<User> getAllNotDeleted() {
        return new ArrayList<>(usersRepo.findAllByDeletedFalse());
    }


    private User getUserFromKeyclock(UUID id){
        RealmResource realmResource = getKeyclockRealmResource();
        UserResource userResource = realmResource.users().get(id.toString());
        User user = null;
        if (userResource != null) {
            UserRepresentation userRepresentation = userResource.toRepresentation();
            user = new User(userRepresentation);
        }
        return user;
    }


    private void updateUserInKeyclock(User user){
        RealmResource realmResource = getKeyclockRealmResource();
        UserResource userResource = realmResource.users().get(user.getId().toString());
        UserRepresentation userRepresentation = updateUserRepresentationFromUser(userResource.toRepresentation(), user);
        userResource.update(userRepresentation);
    }

    private void blockUserInKeyclock(User user) {
        RealmResource realmResource = getKeyclockRealmResource();
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

    // https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547/2
    private RealmResource getKeyclockRealmResource(){
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

}
