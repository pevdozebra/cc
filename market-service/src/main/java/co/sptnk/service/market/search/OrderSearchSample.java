package co.sptnk.service.market.search;


import co.sptnk.service.market.model.Order;
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
public class OrderSearchSample {

    private Example<Order> sample;
    private Pageable pageable;

    private OrderSearchSample() {
    }


    public static OrderSearchSample parse(Map<String, String> params) {
        OrderSearchSample data = new OrderSearchSample();
        try {
            Order order = new Order();
            order.setCustomerId(params.get("customerId") != null ? UUID.fromString(params.get("customerId")) : null);
            order.setPerformerId(params.get("performerId") != null ? UUID.fromString(params.get("performerId")) : null);
//            this.startDate = params.get("startDate") != null ?
//                    LocalDateTime.parse(params.get("startDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
//            this.startDate = params.get("endDate") != null ?
//                    LocalDateTime.parse(params.get("endDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
            data.setPageable(PageRequest.of(
                    params.get("page") != null ? Integer.parseInt(params.get("page")) : 0,
                    params.get("offset") != null ? Integer.parseInt(params.get("count")) : Integer.MAX_VALUE
            ));
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            data.setSample(Example.of(order, matcher));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return data;
    }
}
