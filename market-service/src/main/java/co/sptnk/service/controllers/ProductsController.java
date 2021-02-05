package co.sptnk.service.controllers;

import co.sptnk.lib.base.AbstractCHController;
import co.sptnk.lib.exceptions.ServiceException;
import co.sptnk.lib.keys.AllowedLinksMethods;
import co.sptnk.service.model.Product;
import co.sptnk.service.services.IProductsService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
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
import java.util.UUID;


@Slf4j
@OpenAPIDefinition(info = @Info(description = "Api для взаимодействия с сервисом продуктов и услуг", title = "Market-service"))
@Tag(name = "ProductController", description = "API объекта Product (Продукт/Услуга)")
@RestController
@RequestMapping("products")
public class ProductsController extends AbstractCHController<Product, Long> {

    @Autowired
    IProductsService service;

    @Override
    public ResponseEntity<Product> add(@RequestBody Product product) {
        Product result;
        try {
            result = service.add(product);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Product> update(@RequestBody Product product) {
        Product result;
        try {
            result = createLinks(service.update(product), product.getId(), AllowedLinksMethods.PUT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Product> getOneById(@PathVariable("id") Long id) {
        Product entity;
        try {
            entity = createLinks(service.getOneById(id), id, AllowedLinksMethods.GET);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        try {
            service.delete(id);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Class<? extends AbstractCHController<Product, Long>> getSelfClass() {
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
        try {
            products = service.getAllForUser(UUID.fromString(params.get("userId")));
            products.forEach(p -> createLinks(p, p.getId(), AllowedLinksMethods.GET_ALL));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
    }
}
