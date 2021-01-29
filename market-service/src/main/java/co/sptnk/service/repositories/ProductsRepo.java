package co.sptnk.service.repositories;

import co.sptnk.service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductsRepo extends JpaRepository<Product, Long> {

    @Query("from Product where userId = ?1")
    Set<Product> findAllForUser(Long userId);
}
