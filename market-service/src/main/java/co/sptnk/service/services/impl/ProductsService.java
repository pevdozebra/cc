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
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService implements IProductsService {

    @Autowired
    ProductsRepo productsRepo;

    public List<Product> getAllForUser(UUID uuid) {
        return new ArrayList<>(productsRepo.findAllByPerformerIdAndActiveTrueAndDeletedFalse(uuid));
    }

    public Product add(Product product) throws MarketServiceException{
        if (product.getId() != null) {
            throw new MarketServiceException("Объект с таким ID уже существует");
        }
        return productsRepo.save(product);
    }

    @Override
    public Product update(Product product) throws MarketServiceException {
        if (product.getId() == null) {
            throw new MarketServiceException("Невозможно идентифицировать сохраняемый объект");
        }
        Product exist = productsRepo.findProductByIdAndDeletedFalse(product.getId()).orElse(null);
        if (exist == null) {
            throw new MarketServiceException("Объект для сохранения не найден");
        }
        return productsRepo.save(product);
    }

    @Override
    public Product getOneById(Long productId) throws MarketServiceException {
        Product product = productsRepo.findProductByIdAndDeletedFalse(productId).orElse(null);
        if (product == null) {
            throw new MarketServiceException("Объект не найден");
        }
        return product;
    }

    @Override
    public List<Product> getAll(Map<String, String> params) {
        return null;
    }

    public void delete(Long id) throws MarketServiceException{
        Product product = productsRepo.findProductByIdAndDeletedFalse(id).orElse(null);
        if (product == null || (product.getDeleted() != null && product.getDeleted())) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new MarketServiceException(error);
        }
        product.setDeleted(true);
        productsRepo.save(product);
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
