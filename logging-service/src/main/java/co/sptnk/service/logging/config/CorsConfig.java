package co.sptnk.service.logging.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods(Arrays.stream(HttpMethod.values()).map(Enum::name).toArray(String[]::new))
                .allowedOrigins("http://localhost:8080");
    }
}
