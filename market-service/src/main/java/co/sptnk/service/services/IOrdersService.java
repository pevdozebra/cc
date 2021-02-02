package co.sptnk.service.services;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Order;

import java.util.List;

public interface IOrdersService {

    /**
     * Получение списка не удаленных заказов
     * @return список заказов без признака удаления
     */
    List<Order> getAllNotDeleted();

    /**
     * Сохранения заказа
     * @param order - объект заказа
     */
    void save(Order order);

    /**
     * Получение списка заказов для заказчика
     * @param id - идентификатор заказчика
     * @return - список заказов для заказчика
     */
    List<Order> getCustomerList(Long id);

    /**
     * Получение списка заказов для исполнителя
     * @param id - идентификатор исполнителя
     * @return - список заказов для исполнителя
     */
    List<Order> getPerformerList(Long id);

    /**
     * Удаление заказа (помечает заказ признаком удаления
     * @param id - идентификатор заказа
     * @throws MarketServiceException - если заказ не найден
     */
    void delete(Long id) throws MarketServiceException;
}
