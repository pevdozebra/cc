package co.sptnk.service.services;

import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserService extends AbstractCrudService<User, UUID> {

    /**
     * Получение списка не удаленных пользователей
     * @return - список не удаленных пользователей
     */
    public List<User> getAllNotDeleted();
}
