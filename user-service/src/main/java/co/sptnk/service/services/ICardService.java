package co.sptnk.service.services;

import co.sptnk.lib.base.AbstractCHService;
import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.Card;

import java.util.List;
import java.util.UUID;

public interface ICardService extends AbstractCHService<Card, Long> {

    /**
     * Получение списка карт для пользователя
     * @param userId - идентификатор пользователя
     * @return - список карт для пользователя
     */
    List<Card> getAllForUser(UUID userId) throws UserServiceExeption;

    /**
     * Получение списка не удаленных карт
     * @return - список не удаленных карт
     */
    public List<Card> getAllNotDeleted();

}
