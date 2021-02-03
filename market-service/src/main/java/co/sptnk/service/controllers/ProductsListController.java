package co.sptnk.service.controllers;

import co.sptnk.service.keys.PageableDefaultKeys;
import co.sptnk.service.model.Product;
import co.sptnk.service.services.IProductsService;
import co.sptnk.service.transfers.ProductPageTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("products/list")
public class ProductsListController {

    @Autowired
    IProductsService productsService;

    /**
     * Получение списка продуктов для пользователя
     * @param performerUuid - идентификатор пользователя
     * @return список продуктов для пользователя
     * @throws Exception
     */
    @GetMapping
    @Operation(parameters = {
            @Parameter(name = "performerUuid", description = "Идентификатор пользователя"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов")
    }
    )
    public ResponseEntity<List<Product>> getAllForUser(@RequestParam(value = "performerUuid") UUID performerUuid) throws Exception{

        return ResponseEntity.ok(productsService.getAllForUser(performerUuid));
    }

    /**
     * Получение списка продуктов в системе, постранично
     * @return список продуктов для пользователя
     */
    @GetMapping("/all")
    @Operation(parameters = {
            @Parameter(name = "page", description = "Номер страницы"),
            @Parameter(name = "offset", description = "Количество объектов на страницу")
    }, description = "Метод отдает объекты постранично. Возвращает ссылку на следующую страницу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов")
    }
    )
    public ResponseEntity<ProductPageTO> getAll(@RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "offset", required = false) Integer offset) {
        Integer pageNum = page != null ? page : PageableDefaultKeys.PAGE_NUM;
        Integer offsetCount = offset != null ? offset : PageableDefaultKeys.OFFSET_COUNT;

        ProductPageTO productTO = productsService.getAll(pageNum, offsetCount);

        return ResponseEntity.ok(productTO);
    }
}
