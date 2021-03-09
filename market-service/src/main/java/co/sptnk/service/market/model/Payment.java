package co.sptnk.service.market.model;

import co.sptnk.service.market.ref.PaymentStatus;
import co.sptnk.service.market.ref.PaymentType;
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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Payment")
    @SequenceGenerator(name = "Payment", sequenceName = "payments_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Дата и время платежа
     */
    @Column(name = "pay_date")
    private OffsetDateTime payDate;

    /**
     * Дата и время проведения платежа
     */
    @Column(name = "process_date")
    private OffsetDateTime processDate;

    /**
     * Статус
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * Сумма платежа
     */
    private BigDecimal amount;

    /**
     * Тип платежа
     */
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    /**
     * Идентификатор транзакции
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * Связь с заказом
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    /**
     * Идентификатор плательщика (пользователя)
     */
    @Column(name = "user_id")
    private UUID userId;

    @JsonIgnore
    private Boolean deleted;

    @Version
    @JsonIgnore
    private Long version;
}
