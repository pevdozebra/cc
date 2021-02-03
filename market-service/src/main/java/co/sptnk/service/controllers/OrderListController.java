package co.sptnk.service.controllers;

import co.sptnk.service.model.Order;
import co.sptnk.service.services.IOrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @Operation(description = "Метод отдает объекты постранично. Возвращает ссылку на следующую страницу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов")
    }
    )
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAll() throws Exception{
        return ResponseEntity.ok(ordersService.getAllNotDeleted());
    }

    /**
     * Получение списка заказов для заказчика
     * @param customer - идентификатор заказчика
     * @return список заказов для заказчика
     */
    @Operation(parameters = {
            @Parameter(name = "uuid", description = "UUID заказчика"),
    }, description = "Список заказов для заказчика")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов")
    }
    )
    @GetMapping("/customer")
    public ResponseEntity<List<Order>> getCustomerOrderList(@RequestParam("uuid") UUID customer) {
        return ResponseEntity.ok(ordersService.getCustomerList(customer));
    }

    /**
     * Получение списка заказов для исполнителя
     * @param performer - идентификатор исполнителя
     * @return список заказов для исполнителя
     */
    @Operation(parameters = {
            @Parameter(name = "uuid", description = "UUID исполнителя"),
    }, description = "Список заказов для исполнителя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов")
    }
    )
    @GetMapping("/performer")
    public ResponseEntity<List<Order>> getPerformerOrderList(@RequestParam("uuid") UUID performer) {
        return ResponseEntity.ok(ordersService.getPerformerList(performer));
    }
}
