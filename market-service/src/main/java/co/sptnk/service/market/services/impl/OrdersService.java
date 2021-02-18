package co.sptnk.service.market.services.impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.common.MessageProducer;
import co.sptnk.service.market.model.Order;
import co.sptnk.service.market.repositories.OrdersRepo;
import co.sptnk.service.market.services.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrdersService implements IOrdersService {

    @Autowired
    private ModelMapper mapper;

    private final OrdersRepo ordersRepo;

    private final MessageProducer messageProducer;

    public OrdersService(OrdersRepo ordersRepo, MessageProducer messageProducer) {
        this.ordersRepo = ordersRepo;
        this.messageProducer = messageProducer;
    }

    @Override
    public Order add(Order order) {
        if (order.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        order.setDeleted(false);
        messageProducer.sendLogMessage(
                EventCode.ORDER_CREATE,
                EventType.INFO,
                EventCode.ORDER_CREATE.getDescription()
        );
        return ordersRepo.save(order);
    }

    @Override
    public Order update(Order order) {
        if (order.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Order exist = ordersRepo.findOrderByIdAndDeletedFalse(order.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        messageProducer.sendLogMessage(
                EventCode.ORDER_EDIT,
                EventType.INFO,
                EventCode.ORDER_EDIT.getDescription(exist.getId())
        );
        mapper.map(order, exist);
        return ordersRepo.save(exist);
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
        messageProducer.sendLogMessage(
                EventCode.ORDER_DELETE,
                EventType.INFO,
                EventCode.ORDER_DELETE.getDescription(id)
        );
        order.setDeleted(true);
    }

    @Override
    public Order getOneById(Long orderId) {
        return ordersRepo.findOrderByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Order> getAll(Map<String, String> params) {
        Order order = new Order();
        Example.of(order).getMatcher().withIgnoreNullValues();
        return ordersRepo.findAll();
    }
}
