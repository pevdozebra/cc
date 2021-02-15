package co.sptnk.service.services.impl;

import co.sptnk.lib.common.AMQPKeys;
import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventLogDTO;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.mappers.EntityMapper;
import co.sptnk.service.model.Product;
import co.sptnk.service.repositories.ProductsRepo;
import co.sptnk.service.services.IProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService implements IProductsService {

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EntityMapper<Product, Product> mapper;

    public List<Product> getAllForUser(UUID uuid) {
        return new ArrayList<>(productsRepo.findAllByPerformerIdAndActiveTrueAndDeletedFalse(uuid));
    }

    public Product add(Product product) {
        if (product.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        product.setDeleted(false);
        product.setActive(true);
        EventLogDTO eventLog = new EventLogDTO();
        eventLog.setType(EventType.INFO);
        eventLog.setCode(EventCode.PRODUCT_CREATE);
        eventLog.setEventDate(LocalDateTime.now());
        eventLog.setDescription("Тест добавления продукта 1");
        rabbitTemplate.convertAndSend(AMQPKeys.TOPIC_EVENTLOG, eventLog);
        return productsRepo.save(product);
    }

    @Override
    public Product update(Product product) {
        if (product.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Product exist = productsRepo.findProductByIdAndDeletedFalse(product.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return productsRepo.save(mapper.toEntity(product, exist));
    }

    @Override
    public Product getOneById(Long productId) {
        return productsRepo.findProductByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Product> getAll(Map<String, String> params) {
        return productsRepo.findAll();
    }


    @Transactional
    @Override
    public void delete(Long id) {
        Product product = productsRepo.findProductByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (product.getDeleted() != null && product.getDeleted()) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        product.setDeleted(true);
    }

    /*    public ProductPageTO getAll(Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Product> productPage = productsRepo.findAllByDeletedFalse(paging);
        ProductPageTO productPageTO = new ProductPageTO(productPage.getContent());
        productPageTO.setCurrentPage(productPage.getNumber());
        productPageTO.setTotalItems(productPage.getTotalElements());
        productPageTO.setTotalPages(productPage.getTotalPages());

        if (productPage.getNumber() < productPage.getTotalPages() - 1) {
            productPageTO.getLinks().put("nextPage",
                    LinkSupportUtils.getAllProductsPageableSupport(page + 1, size));
        }
        if (productPage.getNumber() > ProductDefaultKeys.PAGE_NUM) {
            productPageTO.getLinks().put("prevPage",
                    LinkSupportUtils.getAllProductsPageableSupport(page - 1 , size));
        }
        return productPageTO;
    }*/
}
