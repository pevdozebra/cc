package co.sptnk.service.user.services.Impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.user.common.MessageProducer;
import co.sptnk.service.user.common.config.ConfigStore;
import co.sptnk.service.user.model.ServiceConfiguration;
import co.sptnk.service.user.repositories.ConfigRepo;
import co.sptnk.service.user.services.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ConfigService implements IConfigService {

    private final ConfigRepo configRepo;

    private final ConfigStore configStore;

    private final MessageProducer message;

    @Autowired
    private ModelMapper mapper;

    public ConfigService(ConfigRepo repo, ConfigStore store, MessageProducer message) {
        this.configRepo = repo;
        this.configStore = store;
        this.message = message;
    }

    @Override
    @Transactional
    public ServiceConfiguration update(ServiceConfiguration conf) {
        ServiceConfiguration storedConfig = configRepo.findById(conf.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mapper.map(conf, storedConfig);
        configStore.putConfig(conf.getId(), conf.getValueType(), conf.getValue());
        message.sendLogMessage(
                EventCode.USER_SERVICE_CONFIG_UPDATE,
                EventType.INFO,
                EventCode.USER_SERVICE_CONFIG_UPDATE.getDescription() +
                        " Конфигурация с id: " + conf.getId()
        );
        return storedConfig;
    }

    @Override
    public Iterable<ServiceConfiguration> getAll() {
        List<ServiceConfiguration> result = configRepo.findAll();
        syncConfig(result);
        return result;
    }

    private void syncConfig(List<ServiceConfiguration> config) {
        config.forEach(c -> {
            configStore.putConfig(c.getId(), c.getValueType(), c.getValue());
        });
        log.info("Выполнена синхронизация конфигурации с базой данных");
    }
}
