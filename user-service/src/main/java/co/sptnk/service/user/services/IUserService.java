package co.sptnk.service.user.services;

import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.user.model.Interest;
import co.sptnk.service.user.model.Role;
import co.sptnk.service.user.model.User;


import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IUserService extends AbstractCrudService<User, UUID> {

    /**
     * Получение списка не удаленных пользователей
     * @return - список не удаленных пользователей
     */
    List<User> getAllNotDeleted();

    /**
     * Добавление интересов для пользователя
     * @param ids - список Id добавляемых интересов
     * @param userId - Id пользователя
     * @return - итоговый список всех интересов пользователя после добавления
     */
    Set<Interest> addInterests(Set<Long> ids, UUID userId);

    /**
     * Удаление интересов у пользователя
     * @param ids - список Id удаляемых интересов
     * @param userId - Id пользователя
     * @return - итоговый список всех интересов пользователя после удаления
     */
    Set<Interest> deleteInterests(Set<Long> ids, UUID userId);

    /**
     * Добавление ролей для пользователя
     * @param roles - список c именами добавляемых ролей
     * @param userId - Id пользователя
     * @return - итоговый список всех ролей пользователя после добавления
     */
    Set<Role> addRoles(Set<String> roles, UUID userId);

    /**
     * Удаление ролей у пользователя
     * @param roles - список c именами удаляемых ролей
     * @param userId - Id пользователя
     * @return - итоговый список всех ролей пользователя после удаления
     */
    Set<Role> deleteRoles(Set<String> roles, UUID userId);
}
