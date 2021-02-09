package co.sptnk.service.services;

import co.sptnk.lib.base.AbstractCHService;
import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.PerformerRating;

import java.util.List;
import java.util.UUID;

public interface IPerformerRatingService extends AbstractCHService<PerformerRating, Long> {

    /**
     * Получение списка рейтингов для исполнителя
     * @param userId - идентификатор исполнителя
     * @return - списка рейтингов для исполнителя
     */
    List<PerformerRating> findAllByPerformer(UUID userId) throws UserServiceExeption;

    /**
     * Получение списка не удаленных рейтингов
     * @return - список не удаленных рейтингов
     */
    public List<PerformerRating> getAllNotDeleted();


}
