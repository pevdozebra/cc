package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
import co.sptnk.service.model.PerformerVerification;
import co.sptnk.service.services.IPerformerVerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "PerformerVerificationController", description = "API для объекта PerformerVerification (Верификация)")
@RestController
@RequestMapping("verification")
public class PerformerVerificationController extends AbstractCrudHateoasController<PerformerVerification, Long> {

    @Autowired
    private IPerformerVerificationService service;

    @GetMapping("/{id}")
    public ResponseEntity<PerformerVerification> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerVerification> add(@RequestBody PerformerVerification performerVerification) {
        PerformerVerification result;
        result = service.add(performerVerification);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PerformerVerification> update(@RequestBody  PerformerVerification performerVerification) {
        return new ResponseEntity<>( createLinks(service.update(performerVerification), performerVerification.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerVerification> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam  Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<PerformerVerification, Long>> getSelfClass() {
        return this.getClass();
    }
}
