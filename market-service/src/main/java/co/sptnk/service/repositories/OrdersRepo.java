package co.sptnk.service.repositories;

import co.sptnk.service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface OrdersRepo extends JpaRepository<Order, Long> {

    Collection<Order> findAllByCustomerIdAndDeletedFalse(UUID uuid);

    Collection<Order> findAllByPerformerIdAndDeletedFalse(UUID uuid);

    Collection<Order> findAllByDeletedFalse();
}
