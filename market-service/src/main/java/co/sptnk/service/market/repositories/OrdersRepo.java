package co.sptnk.service.market.repositories;

import co.sptnk.service.market.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepo extends JpaRepository<Order, Long> {

    Collection<Order> findAllByCustomerIdAndDeletedFalse(UUID uuid);

    Collection<Order> findAllByPerformerIdAndDeletedFalse(UUID uuid);

    Optional<Order> findOrderByIdAndDeletedFalse(Long id);

    Collection<Order> findAllByDeletedFalse();
}
