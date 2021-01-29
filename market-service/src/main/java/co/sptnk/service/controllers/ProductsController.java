package co.sptnk.service.controllers;

import co.sptnk.service.model.Product;
import co.sptnk.service.repositories.ProductsRepo;
import co.sptnk.service.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("products")
public class ProductsController {

    @Autowired
    ProductsRepo productsRepo;

    @Autowired
    ProductsService productsService;

    /**
     * Получение списка продуктов для пользователя
     * @param userId - идентификатор пользователя
     * @return список продуктов для пользователя
     * @throws Exception
     */
    @GetMapping("/list/{userId}")
    public ResponseEntity<Set<Product>> getAllForUser(@PathVariable("userId") Long userId) throws Exception{
        return ResponseEntity.ok(productsService.getAllForUser(userId));
    }

    /**
     * Добавление/обновление продукта
     * @param product - продукт
     * @throws Exception
     */
    @PutMapping("/add")
    public void update(@RequestBody Product product) throws Exception{
        productsService.save(product);
    }

    /**
     * Удаление продукта по идентификатору
     * @param id - идетификатор продукта
     * @throws Exception
     */
    @DeleteMapping("/delete/{productId}")
    public void delete(@PathVariable("productId") Long id) throws Exception{
        productsService.delete(id);
    }
}
