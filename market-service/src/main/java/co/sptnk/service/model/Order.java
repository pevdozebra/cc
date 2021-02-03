package co.sptnk.service.model;

import co.sptnk.service.keys.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order")
    @SequenceGenerator(name = "Order", sequenceName = "orders_id_seq", allocationSize = 1)
    Long id;

    /**
     * Идентификатор заказчика
     */
    @Column(name = "customer_id")
    UUID customerId;

    /**
     * Идентификатор исполнителя
     */
    @Column(name = "performer_id")
    UUID performerId;

    /**
     * Продукт для заказа
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "product_id")
    Product productId;

    /**
     * Дата и время заказа
     */
    @Column(name = "order_date")
    LocalDateTime orderDate;

    /**
     * Дата и время исполнения
     */
    @Column(name = "perform_date")
    LocalDateTime performDate;

    /**
     * Статус заказа
     */
    @Enumerated(EnumType.STRING)
    OrderStatus status;

    /**
     * Дата и время изменения статуса
     */
    @Column(name = "status_update")
    LocalDateTime statusUpdateDate;

    /**
     * Признак удаленного заказа
     * Исключено из трансфера
     */
    @JsonIgnore
    Boolean deleted = false;


}
