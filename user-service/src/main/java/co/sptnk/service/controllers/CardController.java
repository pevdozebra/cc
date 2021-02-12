package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.model.Card;
import co.sptnk.service.services.ICardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "CardController", description = "API для объекта Card (Карта)")
@RestController
@RequestMapping("cards")
public class CardController extends AbstractCrudHateoasController<Card, Long> {

    @Autowired
    private ICardService service;

    @GetMapping("/{id}")
    public ResponseEntity<Card> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Card> add(@RequestBody Card card) {
        Card result;
        result = service.add(card);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Card> update(@RequestBody Card card) {
        return new ResponseEntity<>( createLinks(service.update(card), card.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Card> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAllNotDeleted(), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<Card, Long>> getSelfClass() {
        return this.getClass();
    }
}
