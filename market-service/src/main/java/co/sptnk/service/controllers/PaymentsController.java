package co.sptnk.service.controllers;

import co.sptnk.lib.constant.AllowedLinksMethods;
import co.sptnk.lib.controller.AbstractCrudHateoasController;
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
public class PaymentsController extends AbstractCrudHateoasController<Payment, Long> {

    @Autowired
    IPaymentsService service;


    @Override
    public ResponseEntity<Payment> add(@RequestBody Payment payment) {
        Payment result;
        result = service.add(payment);
        result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Payment> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(createLinks(service.getOneById(id), id, AllowedLinksMethods.GET),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Payment> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Class<? extends AbstractCrudHateoasController<Payment, Long>> getSelfClass() {
        return this.getClass();
    }

    @Override
    public ResponseEntity<Payment> update(@RequestBody Payment payment) {
        return new ResponseEntity<>(createLinks(service.update(payment), payment.getId(), AllowedLinksMethods.PUT),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }
}
