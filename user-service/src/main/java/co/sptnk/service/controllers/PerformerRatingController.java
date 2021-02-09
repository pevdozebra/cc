package co.sptnk.service.controllers;

import co.sptnk.lib.base.AbstractCHController;
import co.sptnk.lib.exceptions.ServiceException;
import co.sptnk.lib.keys.AllowedLinksMethods;
import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.services.IPerformerRatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "PerformerRatingController", description = "API для объекта PerformerRating (Оценка исполнителя)")
@RestController
@RequestMapping("ratings")
public class PerformerRatingController extends AbstractCHController<PerformerRating, Long> {

    @Autowired
    private IPerformerRatingService service;

    @GetMapping("/{id}")
    public ResponseEntity<PerformerRating> getOneById(@PathVariable("id") Long id) {
        PerformerRating entity;
        try {
            entity = createLinks(service.getOneById(id), id, AllowedLinksMethods.GET);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/findAllByPerformer/{userId}")
    public ResponseEntity findAllByPerformer(@PathVariable("userId") UUID id) {
        List<PerformerRating> result;
        try{
            result = service.findAllByPerformer(id);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerRating> add(@RequestBody PerformerRating performerRating) {
        PerformerRating result;
        try {
            result = service.add(performerRating);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PerformerRating> update(@RequestBody PerformerRating performerRating) {
        PerformerRating result;
        try {
            result = createLinks(service.update(performerRating), performerRating.getId(), AllowedLinksMethods.PUT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerRating> delete(@RequestBody Long id) {
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
    protected Class<? extends AbstractCHController<PerformerRating, Long>> getSelfClass() {
        return this.getClass();
    }
}
