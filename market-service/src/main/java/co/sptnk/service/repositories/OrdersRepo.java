package co.sptnk.service.repositories;

import co.sptnk.service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrdersRepo extends JpaRepository<Order, Long> {

    Collection<Order> findAllByCustomerIdAndDeletedFalse(Long id);

    Collection<Order> findAllByPerformerIdAndDeletedFalse(Long id);

    Collection<Order> findAllByDeletedFalse();
}
