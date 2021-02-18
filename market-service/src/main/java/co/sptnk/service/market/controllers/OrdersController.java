package co.sptnk.service.market.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.market.model.Order;
import co.sptnk.service.market.services.IOrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "OrdersController", description = "API объекта Order (Заказ)")
@RestController
@RequestMapping("orders")
public class OrdersController extends AbstractCrudHateoasController<Order, Long> {

    @Autowired
    private IOrdersService service;


    @Override
    public ResponseEntity<Order> add(@RequestBody Order order) {
        Order result;
        result = service.add(order);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Class<? extends AbstractCrudHateoasController<Order, Long>> getSelfClass() {
        return this.getClass();
    }

    @Override
    public ResponseEntity<Order> update(@RequestBody Order order) {
        return new ResponseEntity<>( createLinks(service.update(order), order.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }
}
