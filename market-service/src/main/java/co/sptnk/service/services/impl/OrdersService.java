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

@Slf4j
@Service
public class OrdersService implements IOrdersService {

    @Autowired
    private OrdersRepo ordersRepo;

    public List<Order> getAllNotDeleted() {
        return new ArrayList<>(ordersRepo.findAllByDeletedFalse());
    }

    public void save(Order order) {
        ordersRepo.save(order);
    }

    public List<Order> getCustomerList(Long id) {
        return new ArrayList<>(ordersRepo.findAllByCustomerIdAndDeletedFalse(id));
    }

    public List<Order> getPerformerList(Long id) {
        return new ArrayList<>(ordersRepo.findAllByPerformerIdAndDeletedFalse(id));
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
}
