package co.sptnk.service.controllers;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Product;
import co.sptnk.service.services.IProductsService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductsController {

    @Autowired
    IProductsService productsService;

    /**
     * Получение списка продуктов для пользователя
     * @param userId - идентификатор пользователя
     * @return список продуктов для пользователя
     * @throws Exception
     */
    @GetMapping("/list/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов"),
    }
    )
    public ResponseEntity<List<Product>> getAllForUser(@PathVariable("userId") Long userId) throws Exception{
        return ResponseEntity.ok(productsService.getAllForUser(userId));
    }

    /**
     * Добавление/обновление продукта
     * @param product - продукт
     * @throws Exception
     */
    @PutMapping("/add")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объект успешно сохранен")
    }
    )
    public void save(@RequestBody Product product) throws Exception{
        productsService.save(product);
    }

    /**
     * Удаление продукта по идентификатору
     * @param id - идетификатор продукта
     * @throws MarketServiceException - если удаляемый объект не найден
     */
    @DeleteMapping("/delete/{productId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Объект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Объект не найден")
    }
    )
    public ResponseEntity delete(@PathVariable("productId") Long id) {
        try {
            productsService.delete(id);
        } catch (MarketServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
