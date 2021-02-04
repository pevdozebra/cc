package co.sptnk.service.services.impl;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.model.ProductType;
import co.sptnk.service.repositories.ProductTypeRepo;
import co.sptnk.service.services.IProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductTypeService implements IProductTypeService {

    @Autowired
    ProductTypeRepo productTypeRepo;

    @Override
    public ProductType add(ProductType productType) throws MarketServiceException {
        if (productType.getId() != null) {
            throw new MarketServiceException("Объект с таким ID уже существует");
        }
        return productTypeRepo.save(productType);
    }

    @Override
    public ProductType update(ProductType productType) throws MarketServiceException {
        if (productType.getId() == null) {
            throw new MarketServiceException("Невозможно идентифицировать сохраняемый объект");
        }
        ProductType exist = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(productType.getId()).orElse(null);
        if (exist == null) {
            throw new MarketServiceException("Объект для сохранения не найден");
        }
        return productTypeRepo.save(productType);
    }

    @Override
    public void delete(Long id) throws MarketServiceException {
        ProductType type = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(id).orElse(null);
        if (type == null || (type.getDeprecated() != null && type.getDeprecated())) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new MarketServiceException(error);
        }
        type.setDeprecated(true);
        productTypeRepo.save(type);
    }

    @Override
    public ProductType getOneById(Long id) throws MarketServiceException {
        ProductType type = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(id).orElse(null);
        if (type == null) {
            throw new MarketServiceException("Объект не найден");
        }
        return type;
    }

    @Override
    public List<ProductType> getAll(Map<String, String> params) {
        return productTypeRepo.findAll();
    }
}
