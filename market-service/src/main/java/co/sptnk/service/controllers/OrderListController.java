package co.sptnk.service.controllers;

import co.sptnk.service.model.Order;
import co.sptnk.service.services.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("orders/list")
public class OrderListController {

    @Autowired
    private IOrdersService ordersService;

    /**
     * Получение всех заказов без признака удаления
     * @return - Возвращает список всех заказов
     * @throws Exception
     */
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAll() throws Exception{
        return ResponseEntity.ok(ordersService.getAllNotDeleted());
    }

    /**
     * Получение списка заказов для заказчика
     * @param customerId - идентификатор заказчика
     * @return список заказов для заказчика
     */
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Order>> getCustomerOrderList(@PathVariable("id") Long customerId) {
        return ResponseEntity.ok(ordersService.getCustomerList(customerId));
    }

    /**
     * Получение списка заказов для исполнителя
     * @param performerId - идентификатор исполнителя
     * @return список заказов для исполнителя
     */
    @GetMapping("/performer/{id}")
    public ResponseEntity<List<Order>> getPerformerOrderList(@PathVariable("id") Long performerId) {
        return ResponseEntity.ok(ordersService.getPerformerList(performerId));
    }
}
