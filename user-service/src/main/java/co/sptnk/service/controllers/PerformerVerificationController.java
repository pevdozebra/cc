package co.sptnk.service.controllers;

import co.sptnk.lib.base.AbstractCHController;
import co.sptnk.lib.exceptions.ServiceException;
import co.sptnk.lib.keys.AllowedLinksMethods;
import co.sptnk.service.model.PerformerVerification;
import co.sptnk.service.services.IPerformerVerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "PerformerVerificationController", description = "API для объекта PerformerVerification (Верификация)")
@RestController
@RequestMapping("verification")
public class PerformerVerificationController extends AbstractCHController<PerformerVerification, Long> {

    @Autowired
    private IPerformerVerificationService service;


    @GetMapping("/{id}")
    public ResponseEntity<PerformerVerification> getOneById(@PathVariable("id") Long id) {
        PerformerVerification entity;
        try {
            entity = createLinks(service.getOneById(id), id, AllowedLinksMethods.GET);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerVerification> add(@RequestBody PerformerVerification performerVerification) {
        PerformerVerification result;
        try {
            result = service.add(performerVerification);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PerformerVerification> update(@RequestBody  PerformerVerification performerVerification) {
        PerformerVerification result;
        try {
            result = createLinks(service.update(performerVerification), performerVerification.getId(), AllowedLinksMethods.PUT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerVerification> delete(@RequestBody Long id) {
        try {
            service.delete(id);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAllNotDeleted(), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCHController<PerformerVerification, Long>> getSelfClass() {
        return this.getClass();
    }
}
