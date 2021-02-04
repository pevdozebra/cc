package co.sptnk.service.controllers;

import co.sptnk.service.base.AbstractCHController;
import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.keys.AllowedLinksMethods;
import co.sptnk.service.model.Order;
import co.sptnk.service.services.IOrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "OrdersController", description = "API объекта Order (Заказ)")
@RestController
@RequestMapping("orders")
public class OrdersController extends AbstractCHController<Order, Long> {

    @Autowired
    private IOrdersService service;

    public OrdersController() {
        init(this);
    }

    @Override
    public ResponseEntity<Order> add(Order order) {
        Order result;
        try {
            result = service.add(order);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOneById(@PathVariable("id") Long id) {
        Order entity;
        try {
            entity = createLinks(service.getOneById(id), id, AllowedLinksMethods.GET);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> delete(@RequestParam("id") Long id) {
        try {
            service.delete(id);
        } catch (MarketServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Order> update(Order order) {
        Order result;
        try {
            result = createLinks(service.update(order), order.getId(), AllowedLinksMethods.PUT);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity(service.getAll(map), HttpStatus.OK);
    }
}
