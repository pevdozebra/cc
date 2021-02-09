package co.sptnk.service.repositories;

import co.sptnk.service.model.PerformerRating;
import co.sptnk.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PerformerRatingsRepo extends JpaRepository<PerformerRating, Long> {

    Collection<PerformerRating> findAllByDeletedFalse();

    Collection<PerformerRating> findPerformerRatingByRatedAndDeletedFalse(User rated);

    Optional<PerformerRating> findPerformerRatingByIdAndDeletedFalse(Long id);

}
