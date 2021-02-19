package co.sptnk.lib.service;


import java.util.Map;

public interface AbstractCrudService<Entity, ID> {

    /**
     * Добавление нового объекта
     * @param entity - Добавляемый объект
     */
    Entity add(Entity entity);

    /**
     * Обновление существующего объекта
     * @param entity - обновляемый объект
     * @return - обновленный объект
     */
    Entity update(Entity entity);

    /**
     * Удаление объекта по идентификатору
     * @param id - идентификатор объекта
     */
    void delete(ID id);

    /**
     * Получение объекта по идентификатору
     * @param id - идентификатор объекта
     * @return - полученный по идентификатору объект
     */
    Entity getOneById(ID id);

    /**
     * Получение всех объектов по фильтру
     * @param params - параметры фильтра
     * @return - список объектов результата
     */
    Iterable<Entity> getAll(Map<String, String> params);
}
