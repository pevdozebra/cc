package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.ValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationCodeRepo extends JpaRepository<ValidationCode, Long> {
}
