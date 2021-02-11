package co.sptnk.service.services.impl;

import co.sptnk.service.mappers.EntityMapper;
import co.sptnk.service.model.Payment;
import co.sptnk.service.repositories.PaymentsRepo;
import co.sptnk.service.services.IPaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentsService implements IPaymentsService {

    @Autowired
    private PaymentsRepo paymentsRepo;

    @Autowired
    private EntityMapper<Payment, Payment> mapper;

    @Override
    public Payment add(Payment payment) {
        if (payment.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return paymentsRepo.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        if (payment.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Payment exist = paymentsRepo.findPaymentByIdAndDeletedFalse(payment.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return paymentsRepo.save(mapper.toEntity(payment, exist));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Payment payment = paymentsRepo.findPaymentByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (payment.getDeleted() != null && payment.getDeleted()) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        payment.setDeleted(true);
    }

    @Override
    public Payment getOneById(Long paymentId) {
        return paymentsRepo.findPaymentByIdAndDeletedFalse(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Payment> getAll(Map<String, String> params) {
        return paymentsRepo.findAll();
    }
}
