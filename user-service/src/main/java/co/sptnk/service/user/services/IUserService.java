package co.sptnk.service.user.services;

import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.user.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserService extends AbstractCrudService<User, UUID> {

    /**
     * Получение списка не удаленных пользователей
     * @return - список не удаленных пользователей
     */
    List<User> getAllNotDeleted();
}
