package co.sptnk.service.market.model;

import co.sptnk.service.market.ref.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order {

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
    private OffsetDateTime orderDate;

    /**
     * Дата и время исполнения
     */
    @Column(name = "perform_date")
    private OffsetDateTime performDate;

    /**
     * Статус заказа
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Дата и время изменения статуса
     */
    @Column(name = "status_update")
    private OffsetDateTime statusUpdateDate;

    /**
     * Признак удаленного заказа
     * Исключено из трансфера
     */
    @JsonIgnore
    private Boolean deleted;

    @Version
    @JsonIgnore
    private Long version;


}
