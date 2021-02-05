package co.sptnk.service.controllers;

import co.sptnk.service.base.AbstractCHController;
import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.keys.AllowedLinksMethods;
import co.sptnk.service.model.Payment;
import co.sptnk.service.services.IPaymentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "PaymentsController", description = "API для объекта Payment")
@RestController
@RequestMapping("payment")
public class PaymentsController extends AbstractCHController<Payment, Long> {

    @Autowired
    IPaymentsService service;


    @Override
    public ResponseEntity<Payment> add(Payment payment) {
        Payment result;
        try {
            result = service.add(payment);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Payment> getOneById(@PathVariable("id") Long id) {
        Payment entity;
        try {
            entity = createLinks(service.getOneById(id), id, AllowedLinksMethods.GET);
        } catch (MarketServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Payment> delete(@PathVariable("id") Long id) {
        try {
            service.delete(id);
        } catch (MarketServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Class<? extends AbstractCHController<Payment, Long>> getSelfClass() {
        return this.getClass();
    }

    @Override
    public ResponseEntity<Payment> update(Payment payment) {
        Payment result;
        try {
            result = createLinks(service.update(payment), payment.getId(), AllowedLinksMethods.PUT);
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
