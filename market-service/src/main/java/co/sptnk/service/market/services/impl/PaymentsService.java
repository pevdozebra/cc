package co.sptnk.service.market.services.impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.market.common.MessageProducer;
import co.sptnk.service.market.model.Payment;
import co.sptnk.service.market.repositories.PaymentsRepo;
import co.sptnk.service.market.services.IPaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private ModelMapper mapper;

    private final PaymentsRepo paymentsRepo;

    private final MessageProducer messageProducer;

    public PaymentsService(PaymentsRepo paymentsRepo, MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
        this.paymentsRepo = paymentsRepo;
    }

    @Override
    public Payment add(Payment payment) {
        if (payment.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        payment.setDeleted(false);
        messageProducer.sendLogMessage(
                EventCode.PAYMENT_CREATE,
                EventType.INFO,
                EventCode.PAYMENT_CREATE.getDescription()
        );
        return paymentsRepo.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        if (payment.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Payment exist = paymentsRepo.findPaymentByIdAndDeletedFalse(payment.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        messageProducer.sendLogMessage(
                EventCode.PAYMENT_EDIT,
                EventType.INFO,
                EventCode.PAYMENT_EDIT.getDescription(exist.getId())
        );
        mapper.map(payment, exist);
        return paymentsRepo.save(exist);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Payment payment = paymentsRepo.findPaymentByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (payment.getDeleted() != null && payment.getDeleted()) {
            String error = "???? ???????????? ?????????????????? ?????????????? ?? id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        messageProducer.sendLogMessage(
                EventCode.PAYMENT_DELETE,
                EventType.INFO,
                EventCode.PAYMENT_DELETE.getDescription(id)
        );
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
