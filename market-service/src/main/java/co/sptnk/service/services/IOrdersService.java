package co.sptnk.service.services;

import co.sptnk.lib.base.AbstractCHService;
import co.sptnk.service.model.Order;

import java.util.List;
import java.util.UUID;

public interface IOrdersService extends AbstractCHService<Order, Long> {

    /**
     * Получение списка не удаленных заказов
     * @return список заказов без признака удаления
     */
    List<Order> getAllNotDeleted();

    /**
     * Получение списка заказов для заказчика
     * @param uuid - идентификатор заказчика
     * @return - список заказов для заказчика
     */
    List<Order> getCustomerList(UUID uuid);

    /**
     * Получение списка заказов для исполнителя
     * @param uuid - идентификатор исполнителя
     * @return - список заказов для исполнителя
     */
    List<Order> getPerformerList(UUID uuid);
}
