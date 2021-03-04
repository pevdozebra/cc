package co.sptnk.service.user.services;

import co.sptnk.service.user.model.ServiceConfiguration;

public interface IConfigService {

    ServiceConfiguration update(ServiceConfiguration conf);
    Iterable<ServiceConfiguration> getAll();
}
