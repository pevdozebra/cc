package co.sptnk.service.market.search;

import co.sptnk.service.market.model.Product;
import co.sptnk.service.market.model.ProductType;
import co.sptnk.service.market.ref.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ProductSearchSample {
    private Example<Product> sample;
    private Pageable pageable;

    private ProductSearchSample() {
    }


    public static ProductSearchSample parse(Map<String, String> params) {
        ProductSearchSample data = new ProductSearchSample();
        try {
            Product product = new Product();
            product.setPerformerId(params.get("performerId") != null ? UUID.fromString(params.get("performerId")) : null);
            product.setStatus(params.get("status") != null ? ProductStatus.valueOf(params.get("status")) : null);
            if (params.get("type") != null) {
                ProductType type = new ProductType();
                type.setTitle(params.get("type"));
                product.setType(type);
            }
            product.setDeleted(false);
            product.setActive(true);
//            this.startDate = params.get("startDate") != null ?
//                    LocalDateTime.parse(params.get("startDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
//            this.startDate = params.get("endDate") != null ?
//                    LocalDateTime.parse(params.get("endDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
            data.setPageable(PageRequest.of(
                    params.get("page") != null ? Integer.parseInt(params.get("page")) : 0,
                    params.get("count") != null ? Integer.parseInt(params.get("count")) : Integer.MAX_VALUE
            ));
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            data.setSample(Example.of(product, matcher));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return data;
    }
}
