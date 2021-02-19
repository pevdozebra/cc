package co.sptnk.service.market.controllers;

import co.sptnk.lib.controller.AbstractCrudController;
import co.sptnk.service.market.model.Product;
import co.sptnk.service.market.services.IProductsService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@OpenAPIDefinition(info = @Info(description = "Api для взаимодействия с сервисом продуктов и услуг", title = "Market-service"))
@Tag(name = "ProductController", description = "API объекта Product (Продукт/Услуга)")
@RestController
@RequestMapping("products")
public class ProductsController extends AbstractCrudController<Product, Long> {


    public ProductsController(IProductsService service) {
        super(service);
    }
}
