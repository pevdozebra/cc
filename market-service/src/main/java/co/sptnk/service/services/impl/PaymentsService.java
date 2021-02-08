package co.sptnk.service.services.impl;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Payment;
import co.sptnk.service.repositories.PaymentsRepo;
import co.sptnk.service.services.IPaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentsService implements IPaymentsService {

    @Autowired
    PaymentsRepo paymentsRepo;

    @Override
    public Payment add(Payment payment) throws MarketServiceException {
        if (payment.getId() != null) {
            throw new MarketServiceException("Объект с таким ID уже существует");
        }
        return paymentsRepo.save(payment);
    }

    @Override
    public Payment update(Payment payment) throws MarketServiceException {
        if (payment.getId() == null) {
            throw new MarketServiceException("Невозможно идентифицировать сохраняемый объект");
        }
        Payment exist = paymentsRepo.findPaymentByIdAndDeletedFalse(payment.getId()).orElse(null);
        if (exist == null) {
            throw new MarketServiceException("Объект для сохранения не найден");
        }
        return paymentsRepo.save(payment);
    }

    @Override
    public void delete(Long id) throws MarketServiceException {
        Payment payment = paymentsRepo.findPaymentByIdAndDeletedFalse(id).orElse(null);
        if (payment == null || (payment.getDeleted() != null && payment.getDeleted())) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new MarketServiceException(error);
        }
        payment.setDeleted(true);
        paymentsRepo.save(payment);
    }

    @Override
    public Payment getOneById(Long paymentId) throws MarketServiceException {
        Payment payment = paymentsRepo.findPaymentByIdAndDeletedFalse(paymentId).orElse(null);
        if (payment == null) {
            throw new MarketServiceException("Объект не найден");
        }
        return payment;
    }

    @Override
    public List<Payment> getAll(Map<String, String> params) {
        return paymentsRepo.findAll();
    }
}
