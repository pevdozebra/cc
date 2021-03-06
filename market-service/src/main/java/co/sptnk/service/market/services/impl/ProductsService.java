package co.sptnk.service.market.services.impl;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.market.common.MessageProducer;
import co.sptnk.service.market.model.Product;
import co.sptnk.service.market.ref.ProductStatus;
import co.sptnk.service.market.repositories.ProductsRepo;
import co.sptnk.service.market.search.ProductSearchSample;
import co.sptnk.service.market.services.IProductsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService implements IProductsService {

    @Autowired
    private ModelMapper modelMapper;

    private final ProductsRepo productsRepo;

    private final MessageProducer messageProducer;

    public ProductsService(ProductsRepo productsRepo, MessageProducer producer) {
        this.productsRepo = productsRepo;
        this.messageProducer = producer;
    }

    public List<Product> getAllForUser(UUID uuid) {
        return new ArrayList<>(productsRepo.findAllByPerformerIdAndActiveTrueAndDeletedFalse(uuid));
    }

    public Product add(Product product) {
        if (product.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        product.setDeleted(false);
        product.setActive(true);
        product.setStatus(ProductStatus.ACTIVE);
        messageProducer.sendLogMessage(
                EventCode.PRODUCT_CREATE,
                EventType.INFO,
                EventCode.PRODUCT_CREATE.getDescription()
        );
        return productsRepo.save(product);
    }

    @Override
    public Product update(Product product) {
        if (product.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Product exist = productsRepo.findProductByIdAndDeletedFalse(product.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        messageProducer.sendLogMessage(
                EventCode.PRODUCT_EDIT,
                EventType.INFO,
                EventCode.PRODUCT_EDIT.getDescription(exist.getId())
        );
        modelMapper.map(product, exist);
        return productsRepo.save(exist);
    }

    @Override
    public Product getOneById(Long productId) {
        return productsRepo.findProductByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<Product> getAll(Map<String, String> params) {
        ProductSearchSample search = ProductSearchSample.parse(params);
        return productsRepo.findAll(search.getSample(), search.getPageable());
    }


    @Transactional
    @Override
    public void delete(Long id) {
        Product product = productsRepo.findProductByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (product.getDeleted() != null && product.getDeleted()) {
            String error = "???? ???????????? ?????????????????? ?????????????? ?? id " + id;
            log.error(error);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        messageProducer.sendLogMessage(
                EventCode.PRODUCT_DELETE,
                EventType.INFO,
                EventCode.PRODUCT_DELETE.getDescription(id)
        );
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
