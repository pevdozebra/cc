package co.sptnk.service.market.controllers;


import co.sptnk.service.market.model.ProductType;
import co.sptnk.service.market.services.IProductTypeService;
import co.sptnk.lib.controller.AbstractCrudController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ProductTypeController", description = "API для объекта ProductType")
@RestController
@RequestMapping("producttype")
public class ProductTypeController extends AbstractCrudController<ProductType, Long> {


    public ProductTypeController(IProductTypeService service) {
        super(service);
    }
}
