package co.sptnk.service.base;

import co.sptnk.service.exceptions.MarketServiceException;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Map;

public interface AbstractCHService<Entity extends RepresentationModel<Entity>, ID> {

    /**
     * Добавление нового объекта
     * @param entity - Добавляемый объект
     */
    Entity add(Entity entity) throws MarketServiceException;

    /**
     * Обновление существующего объекта
     * @param entity - обновляемый объект
     * @return - обновленный объект
     * @throws MarketServiceException - если объект не существует
     */
    Entity update(Entity entity) throws MarketServiceException;

    /**
     * Удаление объекта по идентификатору
     * @param id - идентификатор объекта
     * @throws MarketServiceException - если объект не найден
     */
    void delete(ID id) throws MarketServiceException;

    /**
     * Получение объекта по идентификатору
     * @param id - идентификатор объекта
     * @return - полученный по идентификатору объект
     * @throws MarketServiceException - объект не найден
     */
    Entity getOneById(ID id) throws MarketServiceException;

    /**
     * Получение всех объектов по фильтру
     * @param params - параметры фильтра
     * @return - список объектов результата
     */
    List<Entity> getAll(Map<String, String> params);
}
