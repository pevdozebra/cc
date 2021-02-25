package co.sptnk.service.user.common.utils;

import co.sptnk.service.user.model.User;
import org.keycloak.representations.idm.UserRepresentation;

public class KeyCloakUserUtil {

    public static UserRepresentation updateRepresentationForUser(UserRepresentation userRepresentation, User user) {
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
