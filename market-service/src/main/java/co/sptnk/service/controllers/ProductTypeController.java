package co.sptnk.service.controllers;

import co.sptnk.service.base.AbstractCHController;
import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.keys.AllowedLinksMethods;
import co.sptnk.service.model.ProductType;
import co.sptnk.service.services.IProductTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "ProductTypeController", description = "API для объекта ProductType")
@RestController
@RequestMapping("producttype")
public class ProductTypeController extends AbstractCHController<ProductType, Long> {

    @Autowired
    IProductTypeService service;

    public ProductTypeController() {
        init(this);
    }

    @Override
    public ResponseEntity<ProductType> add(ProductType productType) {
        ProductType result;
        try {
            result = service.add(productType);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductType> getOneById(@PathVariable("id") Long id) {
        ProductType entity;
        try {
            entity = createLinks(service.getOneById(id), id, AllowedLinksMethods.GET);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductType> delete(@RequestParam("id") Long id) {
        try {
            service.delete(id);
        } catch (MarketServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ProductType> update(ProductType productType) {
        ProductType result;
        try {
            result = createLinks(service.update(productType), productType.getId(), AllowedLinksMethods.PUT);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity(service.getAll(map), HttpStatus.OK);
    }
}
