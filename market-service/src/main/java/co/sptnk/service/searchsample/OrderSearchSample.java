package co.sptnk.service.searchsample;

import co.sptnk.service.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchSample {

    private Example<Order> sample;
    private Pageable pageable;


    public void parse(Map<String, String> params) {
        Order order = new Order();
        try {
            order.setCustomerId(params.get("customerId") != null ? UUID.fromString(params.get("customerId")) : null);
            order.setPerformerId(params.get("performerId") != null ? UUID.fromString(params.get("performerId")) : null);
//            this.startDate = params.get("startDate") != null ?
//                    LocalDateTime.parse(params.get("startDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
//            this.startDate = params.get("endDate") != null ?
//                    LocalDateTime.parse(params.get("endDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
            this.pageable = PageRequest.of(
                    params.get("page") != null ? Integer.parseInt(params.get("page")) : 0,
                    params.get("offset") != null ? Integer.parseInt(params.get("count")) : Integer.MAX_VALUE
            );
            this.sample = Example.of(order);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
