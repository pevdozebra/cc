package co.sptnk.service.services.impl;

import co.sptnk.service.mappers.EntityMapper;
import co.sptnk.service.model.ProductType;
import co.sptnk.service.repositories.ProductTypeRepo;
import co.sptnk.service.services.IProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductTypeService implements IProductTypeService {

    @Autowired
    private ProductTypeRepo productTypeRepo;

    @Autowired
    private EntityMapper<ProductType, ProductType> mapper;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        ProductType exist = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(productType.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return productTypeRepo.save(mapper.toEntity(productType, exist));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ProductType type = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (type.getDeprecated() != null && type.getDeprecated()) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        type.setDeprecated(true);
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
