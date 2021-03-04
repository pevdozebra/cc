package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InterestRepo extends JpaRepository<Interest, Long> {

    Collection<Interest> findAllByParent(Interest interest);

    Optional<Interest> findInterestByIdAndDeletedFalse(Long id);

    Collection<Interest> findAllByDeletedFalse();

    Collection<Interest> findByDeletedFalseAndIdIn(Set<Long> id);
}
