package co.sptnk.service.services.impl;

import co.sptnk.service.model.ProductType;
import co.sptnk.service.repositories.ProductTypeRepo;
import co.sptnk.service.services.IProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductTypeService implements IProductTypeService {

    @Autowired
    ProductTypeRepo productTypeRepo;

    @Override
    public ProductType add(ProductType productType) {
        if (productType.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productTypeRepo.save(productType);
    }

    @Override
    public ProductType update(ProductType productType) {
        if (productType.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        productTypeRepo.findProductTypeByIdAndDeprecatedFalse(productType.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return productTypeRepo.save(productType);
    }

    @Override
    public void delete(Long id) {
        ProductType type = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(id).orElse(null);
        if (type == null || (type.getDeprecated() != null && type.getDeprecated())) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        type.setDeprecated(true);
        productTypeRepo.save(type);
    }

    @Override
    public ProductType getOneById(Long id) {
        return productTypeRepo.findProductTypeByIdAndDeprecatedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<ProductType> getAll(Map<String, String> params) {
        return productTypeRepo.findAll();
    }
}
