package co.sptnk.service.controllers;


import co.sptnk.lib.controller.AbstractCrudController;
import co.sptnk.service.model.Payment;
import co.sptnk.service.services.IPaymentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;



@Tag(name = "PaymentsController", description = "API для объекта Payment")
@RestController
@RequestMapping("payment")
public class PaymentsController extends AbstractCrudController<Payment, Long> {


    public PaymentsController(IPaymentsService service) {
        super(service);
    }
}
