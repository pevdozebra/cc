package co.sptnk.service.user.repositories;

import co.sptnk.service.user.common.config.ConfigName;
import co.sptnk.service.user.model.ServiceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepo extends JpaRepository<ServiceConfiguration, ConfigName> {
}
