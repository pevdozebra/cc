package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
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
public class PerformerRatingController extends AbstractCrudHateoasController<PerformerRating, Long> {

    @Autowired
    private IPerformerRatingService service;

    @GetMapping("/{id}")
    public ResponseEntity<PerformerRating> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @GetMapping("/findAllByPerformer/{userId}")
    public ResponseEntity findAllByPerformer(@PathVariable("userId") UUID id) {
        List<PerformerRating> result;
        result = service.findAllByPerformer(id);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PerformerRating> add(@RequestBody PerformerRating performerRating) {
        PerformerRating result;
        result = service.add(performerRating);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PerformerRating> update(@RequestBody PerformerRating performerRating) {
        return new ResponseEntity<>( createLinks(service.update(performerRating), performerRating.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PerformerRating> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAllNotDeleted(), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCrudHateoasController<PerformerRating, Long>> getSelfClass() {
        return this.getClass();
    }
}
