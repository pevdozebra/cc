package co.sptnk.service.market.controllers;

import co.sptnk.service.market.model.Order;
import co.sptnk.service.market.services.IOrdersService;
import co.sptnk.lib.controller.AbstractCrudController;
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
