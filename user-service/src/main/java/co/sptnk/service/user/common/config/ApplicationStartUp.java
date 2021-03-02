package co.sptnk.service.user.common.config;

import co.sptnk.service.user.repositories.ConfigRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class ApplicationStartUp implements ApplicationRunner {

    private final ConfigStore configuration;

    private final ConfigRepo configRepo;

    public ApplicationStartUp(ConfigStore store, ConfigRepo repo) {
        this.configuration = store;
        this.configRepo = repo;
    }

    @Override
    public void run(ApplicationArguments args) {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        configRepo.findAll().forEach(conf -> {
            configuration.putConfig(conf.getId(), conf.getValueType(), conf.getValue());
            count.getAndSet(count.get() + 1);
        });
        log.info(String.format("Загружены записи конфигурации: %s", count));
    }
}
