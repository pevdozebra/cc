package co.sptnk.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_type")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductType")
    @SequenceGenerator(name = "ProductType", sequenceName = "product_type_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Название
     */
    private String title;

    /**
     * Комиссия
     */
    @Column(name = "default_commission")
    private BigDecimal defaultCommission;

    /**
     * Длительность
     */
    @Column(name = "default_duration")
    private Long defaultDuration;

    /**
     * Признак устаревшего продукта
     */
    @JsonIgnore
    private Boolean deprecated;

    @Version
    @JsonIgnore
    private Long version;
}
