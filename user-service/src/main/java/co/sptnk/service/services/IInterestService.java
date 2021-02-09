package co.sptnk.service.services;

import co.sptnk.lib.base.AbstractCHService;
import co.sptnk.service.exceptions.UserServiceExeption;
import co.sptnk.service.model.Card;
import co.sptnk.service.model.Interest;
import java.util.List;

public interface IInterestService extends AbstractCHService<Interest, Long> {

    /**
     * Получение списка интрересов по родителю
     * @param id - идентификатор родительского интереса
     * @return - список карт для пользователя
     */
    List<Interest> findAllByParent(Long id) throws UserServiceExeption;

    /**
     * Получение списка не удаленных интересов
     * @return - список не удаленных интересов
     */
    public List<Interest> getAllNotDeleted();

}
