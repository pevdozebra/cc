package co.sptnk.service.repositories;

import co.sptnk.service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductsRepo extends JpaRepository<Product, Long> {

    Collection<Product> findAllByUserIdAndActiveTrueAndDeletedFalse(Long userId);

    Product findProductByIdAndDeletedFalse(Long id);
}
