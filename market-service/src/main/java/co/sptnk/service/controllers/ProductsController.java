package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.model.Product;
import co.sptnk.service.services.IProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@Tag(name = "ProductController", description = "API объекта Product (Продукт/Услуга)")
@RestController
@RequestMapping("products")
public class ProductsController extends AbstractCrudHateoasController<Product, Long> {

    @Autowired
    IProductsService service;

    @Override
    public ResponseEntity<Product> add(@RequestBody Product product) {
        Product result;
        result = service.add(product);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Product> update(@RequestBody Product product) {
        return new ResponseEntity<>(createLinks(service.update(product), product.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Product> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Class<? extends AbstractCrudHateoasController<Product, Long>> getSelfClass() {
        return ProductsController.class;
    }

    /**
     * Получение списка продуктов
     * @param params - параметры запроса
     * @return список продуктов
     */
    @Operation(parameters = {
            @Parameter(name = "userId", description = "Идентификатор пользователя"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получен список объектов"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры")
    }
    )
    public ResponseEntity<List<Product>> getAll(@RequestParam Map<String, String> params) {
        List<Product> products;
        products = service.getAll(params);
        products.forEach(p -> createLinks(p, p.getId(), AllowedLinksMethods.GET_ALL));
        return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
    }
}
