package co.sptnk.service.logging;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(description = "Api для взаимодействия с сервисом логирования", title = "Logging-service"))
public class LoggingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggingServiceApplication.class, args);
    }
}
