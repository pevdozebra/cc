package co.sptnk.service.services.impl;

import co.sptnk.service.mappers.EntityMapper;
import co.sptnk.service.model.Order;
import co.sptnk.service.repositories.OrdersRepo;
import co.sptnk.service.services.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OrdersService implements IOrdersService {

    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private EntityMapper<Order, Order> mapper;

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
    public Order add(Order order) {
        if (order.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ordersRepo.save(order);
    }

    @Override
    public Order update(Order order) {
        if (order.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Order exist = ordersRepo.findOrderByIdAndDeletedFalse(order.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ordersRepo.save(mapper.toEntity(order, exist));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Order order = ordersRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (order.getDeleted() != null && order.getDeleted()) {
            String error = "Не найден удаляемый заказ с id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        order.setDeleted(true);
    }

    @Override
    public Order getOneById(Long orderId) {
        return ordersRepo.findOrderByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Order> getAll(Map<String, String> params) {
        return ordersRepo.findAll();
    }
}
