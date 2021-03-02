package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.Validation;
import co.sptnk.service.user.model.keys.ValidationPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValidationRepo extends JpaRepository<Validation, ValidationPK> {
    List<Validation> findValidationById_IdAndCodes_Value(String id, String code);
}
