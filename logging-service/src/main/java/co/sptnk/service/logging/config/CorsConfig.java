package co.sptnk.service.logging.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String gatewayHost;

    public CorsConfig(Environment environment) {
        this.gatewayHost = environment.getProperty("server.gateway.host") + ":" +
                environment.getProperty("server.gateway.port");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods(Arrays.stream(HttpMethod.values()).map(Enum::name).toArray(String[]::new))
                .allowedOrigins(gatewayHost);
    }
}
