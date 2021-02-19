package co.sptnk.service.market.model;

import co.sptnk.service.market.ref.PaymentStatus;
import co.sptnk.service.market.ref.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime payDate;

    /**
     * Дата и время проведения платежа
     */
    @Column(name = "process_date")
    private LocalDateTime processDate;

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
