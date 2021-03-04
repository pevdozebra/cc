package co.sptnk.service.market.repositories;

import co.sptnk.service.market.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepo extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByIdAndDeletedFalse(Long id);
}
