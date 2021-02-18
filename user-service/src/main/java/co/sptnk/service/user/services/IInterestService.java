package co.sptnk.service.user.services;

import co.sptnk.lib.service.AbstractCrudService;
import co.sptnk.service.user.model.Interest;
import java.util.List;

public interface IInterestService extends AbstractCrudService<Interest, Long> {

    /**
     * Получение списка интрересов по родителю
     * @param id - идентификатор родительского интереса
     * @return - список карт для пользователя
     */
    List<Interest> findAllByParent(Long id);

    /**
     * Получение списка не удаленных интересов
     * @return - список не удаленных интересов
     */
    List<Interest> getAllNotDeleted();

}
