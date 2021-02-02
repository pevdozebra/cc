package co.sptnk.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order")
    @SequenceGenerator(name = "Order", sequenceName = "orders_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Идентификатор заказчика
     */
    @Column(name="customer_id")
    private Long customerId;

    /**
     * Идентификатор исполнителя
     */
    @Column(name="performer_id")
    private Long performerId;

    /**
     * Признак удаленного заказа
     */
    private Boolean deleted = false;


}
