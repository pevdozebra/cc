package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.model.Interest;
import co.sptnk.service.services.IInterestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "InterestController", description = "API для объекта Interest (Интерес)")
@RestController
@RequestMapping("interests")
public class InterestController extends AbstractCrudHateoasController<Interest, Long> {

    @Autowired
    private IInterestService service;

    @GetMapping("/{id}")
    public ResponseEntity<Interest> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Interest> add(@RequestBody Interest interest) {
        Interest result;
        result = service.add(interest);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Interest> update(@RequestBody Interest interest) {
        return new ResponseEntity<>( createLinks(service.update(interest), interest.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Interest> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam  Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<Interest, Long>> getSelfClass() {
        return this.getClass();
    }
}
