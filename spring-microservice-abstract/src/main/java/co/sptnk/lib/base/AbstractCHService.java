package co.sptnk.lib.base;

import co.sptnk.lib.exceptions.ServiceException;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Map;

public interface AbstractCHService<Entity extends RepresentationModel<Entity>, ID> {

    /**
     * Добавление нового объекта
     * @param entity - Добавляемый объект
     */
    Entity add(Entity entity) throws ServiceException;

    /**
     * Обновление существующего объекта
     * @param entity - обновляемый объект
     * @return - обновленный объект
     * @throws ServiceException - если объект не существует
     */
    Entity update(Entity entity) throws ServiceException;

    /**
     * Удаление объекта по идентификатору
     * @param id - идентификатор объекта
     * @throws ServiceException - если объект не найден
     */
    void delete(ID id) throws ServiceException;

    /**
     * Получение объекта по идентификатору
     * @param id - идентификатор объекта
     * @return - полученный по идентификатору объект
     * @throws ServiceException - объект не найден
     */
    Entity getOneById(ID id) throws ServiceException;

    /**
     * Получение всех объектов по фильтру
     * @param params - параметры фильтра
     * @return - список объектов результата
     */
    List<Entity> getAll(Map<String, String> params);
}
