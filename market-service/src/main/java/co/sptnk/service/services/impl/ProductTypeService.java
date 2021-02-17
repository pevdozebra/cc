package co.sptnk.service.services.impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.common.MessageProducer;
import co.sptnk.service.model.ProductType;
import co.sptnk.service.repositories.ProductTypeRepo;
import co.sptnk.service.services.IProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private ModelMapper mapper;

    private final ProductTypeRepo productTypeRepo;

    private final MessageProducer messageProducer;

    public ProductTypeService(ProductTypeRepo repo, MessageProducer producer) {
        this.productTypeRepo = repo;
        this.messageProducer = producer;
    }

    @Override
    public ProductType add(ProductType productType) {
        if (productType.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productType.setDeprecated(false);
        messageProducer.sendLogMessage(
                EventCode.PRODUCT_TYPE_CREATE,
                EventType.INFO,
                EventCode.PRODUCT_TYPE_CREATE.getDescription()
        );
        return productTypeRepo.save(productType);
    }

    @Override
    public ProductType update(ProductType productType) {
        if (productType.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        ProductType exist = productTypeRepo.findProductTypeByIdAndDeprecatedFalse(productType.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        messageProducer.sendLogMessage(
                EventCode.PRODUCT_TYPE_EDIT,
                EventType.INFO,
                EventCode.PRODUCT_TYPE_EDIT.getDescription(exist.getId())
        );
        mapper.map(productType, exist);
        return productTypeRepo.save(exist);
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
        messageProducer.sendLogMessage(
                EventCode.PRODUCT_TYPE_DELETE,
                EventType.INFO,
                EventCode.PRODUCT_TYPE_DELETE.getDescription(id)
        );
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
