package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.PerformerVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PerformerVerificationsRepo extends JpaRepository<PerformerVerification, Long> {

    Collection<PerformerVerification> findAllByDeletedFalse();

    Optional<PerformerVerification> findPerformerVerificationByIdAndDeletedFalse(Long id);
}
