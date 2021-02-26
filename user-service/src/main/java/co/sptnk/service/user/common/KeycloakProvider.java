package co.sptnk.service.user.common;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakProvider {

    @Autowired
    private Environment environment;
    private Keycloak keycloak;

    public Keycloak get() {
        if (keycloak == null) {
            keycloak = getKeyclockByClient();
        }
        return keycloak;
    }

    // https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547/2
    private Keycloak getKeyclockByClient(){
        return KeycloakBuilder
                .builder()
                .serverUrl(environment.getProperty("keycloak.auth-server-url"))
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(environment.getProperty("keycloak.realm"))
                .clientId(environment.getProperty("keycloak.resource"))
                .clientSecret(environment.getProperty("keycloak.credentials.secret"))
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();
    }

    public AccessTokenResponse getAccessTokenForUser(String userId) {
        String resourceUrl =String.format("%s%s",environment.getProperty("keycloak.auth-server-url"),"/realms/telescope/protocol/openid-connect/token");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.TOKEN_EXCHANGE_GRANT_TYPE);
        map.add(OAuth2Constants.SUBJECT_TOKEN, get().tokenManager().getAccessTokenString());
        map.add(OAuth2Constants.REQUESTED_SUBJECT, userId);
        map.add(OAuth2Constants.CLIENT_ID, environment.getProperty("keycloak.resource"));
        map.add(OAuth2Constants.CLIENT_SECRET, environment.getProperty("keycloak.credentials.secret"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(resourceUrl, request , AccessTokenResponse.class);

        return response.getBody();
    }
}
