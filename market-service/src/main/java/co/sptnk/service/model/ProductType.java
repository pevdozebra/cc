package co.sptnk.service.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "product_type")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductType")
    @SequenceGenerator(name = "ProductType", sequenceName = "product_type_id_seq", allocationSize = 1)
    Long id;

    /**
     * Название
     */
    String title;

    /**
     * Комиссия
     */
    @Column(name = "default_commission")
    BigDecimal defaultCommission;

    /**
     * Длительность
     */
    @Column(name = "defauilt_duration")
    Duration defaultDuration;
}
