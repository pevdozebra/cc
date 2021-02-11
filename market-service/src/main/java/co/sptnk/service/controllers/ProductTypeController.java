package co.sptnk.service.controllers;


import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
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
public class ProductTypeController extends AbstractCrudHateoasController<ProductType, Long> {

    @Autowired
    IProductTypeService service;


    @Override
    public ResponseEntity<ProductType> add(@RequestBody ProductType productType) {
        ProductType result;
        result = service.add(productType);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductType> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductType> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Class<? extends AbstractCrudHateoasController<ProductType, Long>> getSelfClass() {
        return this.getClass();
    }

    @Override
    public ResponseEntity<ProductType> update(@RequestBody ProductType productType) {
        return new ResponseEntity<>(createLinks(service.update(productType), productType.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }
}
