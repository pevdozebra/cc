package co.sptnk.service.repositories;

import co.sptnk.service.model.Interest;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface InterestRepo extends JpaRepository<Interest, Long> {

    Collection<Interest> findAllByParent(Interest interest);

    Optional<Interest> findInterestByIdAndDeletedFalse(Long id);

    Collection<Interest> findAllByDeletedFalse();
}
