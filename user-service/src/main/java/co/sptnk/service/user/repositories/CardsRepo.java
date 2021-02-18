package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.Card;
import co.sptnk.service.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardsRepo extends JpaRepository<Card, Long> {
    Collection<Card> findAllByDeletedFalse();

    Collection<Card> findCardByUserAndDeletedFalse(User user);

    Optional<Card> findCardByIdAndDeletedFalse(Long id);

    Collection<Card> findCardByUser_IdAndDeletedFalse(UUID user_id);
}
