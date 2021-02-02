package co.sptnk.service.controllers;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Order;
import co.sptnk.service.services.IOrdersService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    /**
     * Добавление нового заказа
     * @param order - объект заказа
     */
    @PutMapping("/save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Объект успешно сохранен"),
    }
    )
    public ResponseEntity save(Order order) {
        ordersService.save(order);
        return ResponseEntity.accepted().build();
    }

    /**
     * Установление deleted=true для заказа
     * @param id - идентификатор заказа
     * @throws MarketServiceException - исключение, если удаляемый объект не найден
     */
    @DeleteMapping("/delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public ResponseEntity delete(@RequestParam("orderId") Long id) {
        try {
            ordersService.delete(id);
        } catch (MarketServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
