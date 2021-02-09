package co.sptnk.service.repositories;

import co.sptnk.service.model.Card;
import co.sptnk.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CardsRepo extends JpaRepository<Card, Long> {
    Collection<Card> findAllByDeletedFalse();

    Collection<Card> findCardByUserAndDeletedFalse(User user);

    Optional<Card> findCardByIdAndDeletedFalse(Long id);

}
