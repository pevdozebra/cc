package co.sptnk.service.services.impl;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.Product;
import co.sptnk.service.repositories.ProductsRepo;
import co.sptnk.service.services.IProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductsService implements IProductsService {

    @Autowired
    ProductsRepo productsRepo;

    public List<Product> getAllForUser(Long userId) {
        return new ArrayList<>(productsRepo.findAllByUserIdAndActiveTrueAndDeletedFalse(userId));
    }

    public void save(Product product) {
        productsRepo.save(product);
    }

    public void delete(Long id) throws MarketServiceException{
        Product product = productsRepo.findById(id).orElse(null);
        if (product == null || (product.getDeleted() != null && product.getDeleted())) {
            throw new MarketServiceException("Не найден удаляемый продукт с id " + id);
        }
        product.setDeleted(true);
        productsRepo.save(product);
    }
}
