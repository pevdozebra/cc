package co.sptnk.service.services.impl;

import co.sptnk.service.exceptions.MarketServiceException;
import co.sptnk.service.keys.PageableDefaultKeys;
import co.sptnk.service.model.Product;
import co.sptnk.service.repositories.ProductsRepo;
import co.sptnk.service.services.IProductsService;
import co.sptnk.service.transfers.ProductPageTO;
import co.sptnk.service.utils.LinkSupportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ProductPageTO getAll(Integer page, Integer size) {
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
        if (productPage.getNumber() > PageableDefaultKeys.PAGE_NUM) {
            productPageTO.getLinks().put("prevPage",
                    LinkSupportUtils.getAllProductsPageableSupport(page - 1 , size));
        }
        return productPageTO;
    }

    public void save(Product product) {
        productsRepo.save(product);
    }

    public void delete(Long id) throws MarketServiceException{
        Product product = productsRepo.findProductByIdAndDeletedFalse(id);
        if (product == null || (product.getDeleted() != null && product.getDeleted())) {
            String error = "Не найден удаляемый продукт с id " + id;
            log.error(error);
            throw new MarketServiceException(error);
        }
        product.setDeleted(true);
        productsRepo.save(product);
    }
}
