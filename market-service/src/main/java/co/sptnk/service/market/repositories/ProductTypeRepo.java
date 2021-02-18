package co.sptnk.service.market.repositories;

import co.sptnk.service.market.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepo extends JpaRepository<ProductType, Long> {

    Optional<ProductType> findProductTypeByIdAndDeprecatedFalse(Long id);
}
