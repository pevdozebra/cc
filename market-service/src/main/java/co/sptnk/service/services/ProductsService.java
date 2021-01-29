package co.sptnk.service.services;

import co.sptnk.service.model.Product;
import co.sptnk.service.repositories.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProductsService {

    @Autowired
    ProductsRepo productsRepo;

    public Set<Product> getAllForUser(Long userId) {
        return productsRepo.findAllForUser(userId);
    }

    public void save(Product product) {
        productsRepo.save(product);
    }

    public void delete(Long id) {
        productsRepo.delete(productsRepo.getOne(id));
    }
}
