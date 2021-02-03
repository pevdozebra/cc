package co.sptnk.service.repositories;

import co.sptnk.service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface ProductsRepo extends JpaRepository<Product, Long> {

    Collection<Product> findAllByPerformerIdAndActiveTrueAndDeletedFalse(UUID uuid);

    Product findProductByIdAndDeletedFalse(Long id);

    Page<Product> findAllByDeletedFalse(Pageable pageable);
}
