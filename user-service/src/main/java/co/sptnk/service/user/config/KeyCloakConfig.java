package co.sptnk.service.user.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

@Lazy
@Configuration
public class KeyCloakConfig {

    private final Environment environment;

    public KeyCloakConfig(Environment environment) {
        this.environment = environment;
    }

    // https://keycloak.discourse.group/t/keycloak-admin-client-in-spring-boot/2547/2
    @Bean
    public RealmResource keyCloakRealm(){
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
        return keycloak.realm(environment.getProperty("keycloak.realm"));
    }
}
