package co.sptnk.service.model;

import co.sptnk.service.ref.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order extends RepresentationModel<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order")
    @SequenceGenerator(name = "Order", sequenceName = "orders_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Идентификатор заказчика
     */
    @Column(name = "customer_id")
    private UUID customerId;

    /**
     * Идентификатор исполнителя
     */
    @Column(name = "performer_id")
    private UUID performerId;

    /**
     * Продукт для заказа
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    /**
     * Дата и время заказа
     */
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    /**
     * Дата и время исполнения
     */
    @Column(name = "perform_date")
    private LocalDateTime performDate;

    /**
     * Статус заказа
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Дата и время изменения статуса
     */
    @Column(name = "status_update")
    private LocalDateTime statusUpdateDate;

    /**
     * Признак удаленного заказа
     * Исключено из трансфера
     */
    @JsonIgnore
    private Boolean deleted = false;

    @Version
    @JsonIgnore
    private Long version;


}
