package co.sptnk.service.services.impl;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Order;
import co.sptnk.service.repositories.OrdersRepo;
import co.sptnk.service.services.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OrdersService implements IOrdersService {

    @Autowired
    private OrdersRepo ordersRepo;

    public List<Order> getAllNotDeleted() {
        return new ArrayList<>(ordersRepo.findAllByDeletedFalse());
    }

    public List<Order> getCustomerList(UUID uuid) {
        return new ArrayList<>(ordersRepo.findAllByCustomerIdAndDeletedFalse(uuid));
    }

    public List<Order> getPerformerList(UUID uuid) {
        return new ArrayList<>(ordersRepo.findAllByPerformerIdAndDeletedFalse(uuid));
    }

    @Override
    public Order add(Order order) throws MarketServiceException {
        if (order.getId() != null) {
            throw new MarketServiceException("Объект с таким ID уже существует");
        }
        return ordersRepo.save(order);
    }

    @Override
    public Order update(Order order) throws MarketServiceException {
        if (order.getId() == null) {
            throw new MarketServiceException("Невозможно идентифицировать сохраняемый объект");
        }
        Order exist = ordersRepo.findOrderByIdAndDeletedFalse(order.getId()).orElse(null);
        if (exist == null) {
            throw new MarketServiceException("Объект для сохранения не найден");
        }
        return ordersRepo.save(order);
    }

    public void delete(Long id) throws MarketServiceException{
        Order order = ordersRepo.findById(id).orElse(null);
        if (order == null || (order.getDeleted() != null && order.getDeleted())) {
            String error = "Не найден удаляемый заказ с id " + id;
            log.error(error);
            throw new MarketServiceException(error);
        }
        order.setDeleted(true);
        ordersRepo.save(order);
    }

    @Override
    public Order getOneById(Long orderId) throws MarketServiceException {
        Order order = ordersRepo.findOrderByIdAndDeletedFalse(orderId).orElse(null);
        if (order == null) {
            throw new MarketServiceException("Объект не найден");
        }
        return order;
    }

    @Override
    public List<Order> getAll(Map<String, String> params) {
        return ordersRepo.findAll();
    }
}
