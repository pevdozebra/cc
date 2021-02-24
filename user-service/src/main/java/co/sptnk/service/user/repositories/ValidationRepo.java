package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.Validation;
import co.sptnk.service.user.model.keys.ValidationPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepo extends JpaRepository<Validation, ValidationPK> {

}
