package co.sptnk.service.market.model;

import co.sptnk.service.market.ref.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product extends RepresentationModel<Product>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Product")
    @SequenceGenerator(name = "Product", sequenceName = "products_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Название
     */
    private String title;

    /**
     * Комиссия
     */
    private BigDecimal commission;

    /**
     * Длительность
     */
    @Column(name = "duration")
    private Long duration;

    /**
     * Тип продукта
     */
    @ManyToOne
    @JoinColumn(name = "type_id")
    private ProductType type;

    /**
     * Идентификатор пользователя, которому принадлежит продукт
     */
    @Column(name="performer_id")
    private UUID performerId;

    /**
     * Описание
     */
    private String description;

    /**
     * Цена
     */
    private BigDecimal price;

    /**
     * Услуга доступна с
     */
    @Column(name = "start_time")
    private LocalTime startTime;

    /**
     * Услуга доступна до
     */
    @Column(name = "end_time")
    private LocalTime endTime;

    /**
     * Статус (DRAFT, ACTIVE, ARCHIVED)
     */
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    /**
     * Признак удаленного продукта
     * Исключено из трансфера
     */
    @JsonIgnore
    private Boolean deleted = false;

    /**
     * Признак активного продукта
     * Исключено из трансфера
     */
    @JsonIgnore
    private Boolean active = true;

    @Version
    @JsonIgnore
    private Long version;
}
