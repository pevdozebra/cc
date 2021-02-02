package co.sptnk.service.transfers;

import co.sptnk.service.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProductPageTO {

    private List<Product> content;
    private Integer currentPage;
    private Long totalItems;
    private Integer totalPages;
    private Map<String, String> links;

    public ProductPageTO(List<Product> products) {
        this.content = products;
        this.links = new HashMap<>();
    }
}
