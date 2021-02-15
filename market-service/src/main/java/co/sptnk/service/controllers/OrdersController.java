package co.sptnk.service.controllers;

import co.sptnk.lib.controller.AbstractCrudController;
import co.sptnk.service.model.Order;
import co.sptnk.service.services.IOrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@Tag(name = "OrdersController", description = "API объекта Order (Заказ)")
@RestController
@RequestMapping("orders")
public class OrdersController extends AbstractCrudController<Order, Long> {


    public OrdersController(IOrdersService service) {
        super(service);
    }
}
