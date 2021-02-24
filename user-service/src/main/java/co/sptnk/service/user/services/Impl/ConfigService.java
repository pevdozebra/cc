package co.sptnk.service.user.services.Impl;

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

    @Autowired
    private ModelMapper mapper;

    public ConfigService(ConfigRepo repo, ConfigStore store) {
        this.configRepo = repo;
        this.configStore = store;
    }

    @Override
    @Transactional
    public ServiceConfiguration update(ServiceConfiguration conf) {
        ServiceConfiguration storedConfig = configRepo.findById(conf.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mapper.map(conf, storedConfig);
        configStore.putConfig(conf.getId(), conf.getValueType(), conf.getValue());
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
    }
}
