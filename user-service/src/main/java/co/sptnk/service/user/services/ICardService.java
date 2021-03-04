package co.sptnk.service.user.services;

import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.user.model.Card;

import java.util.List;
import java.util.UUID;

public interface ICardService extends AbstractCrudService<Card, Long> {

    /**
     * Получение списка карт для пользователя
     * @param userId - идентификатор пользователя
     * @return - список карт для пользователя
     */
    List<Card> getAllForUser(UUID userId);

    /**
     * Получение списка не удаленных карт
     * @return - список не удаленных карт
     */
    List<Card> getAllNotDeleted();

}
