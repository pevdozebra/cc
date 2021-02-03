package co.sptnk.service.model;

import co.sptnk.service.keys.PaymentStatus;
import co.sptnk.service.keys.PaymentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Payment")
    @SequenceGenerator(name = "Payment", sequenceName = "payments_id_seq", allocationSize = 1)
    Long id;

    /**
     * Дата и время платежа
     */
    @Column(name = "pay_date")
    LocalDateTime payDate;

    /**
     * Дата и время проведения платежа
     */
    @Column(name = "process_date")
    LocalDateTime processDate;

    /**
     * Статус
     */
    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    /**
     * Сумма платежа
     */
    BigDecimal amount;

    /**
     * Тип платежа
     */
    @Enumerated(EnumType.STRING)
    PaymentType type;

    /**
     * Идентификатор транзакции
     */
    @Column(name = "transaction_id")
    String transactionId;

    /**
     * Связь с заказом
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "order_id")
    Order orderId;

    /**
     * Идентификатор плательщика (пользователя)
     */
    @Column(name = "user_id")
    UUID userId;
}
