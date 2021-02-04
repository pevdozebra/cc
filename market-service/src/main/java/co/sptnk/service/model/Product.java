package co.sptnk.service.model;

import co.sptnk.service.keys.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.util.Duration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="products")
public class Product extends RepresentationModel<Product>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Product")
    @SequenceGenerator(name = "Product", sequenceName = "products_id_seq", allocationSize = 1)
    Long id;

    /**
     * Название
     */
    String title;

    /**
     * Комиссия
     */
    BigDecimal commission;

    /**
     * Длительность
     */
    @Column(name = "duration")
    Long duration;

    /**
     * Тип продукта
     */
    @ManyToOne
    @JoinColumn(name = "type_id")
    ProductType type;

    /**
     * Идентификатор пользователя, которому принадлежит продукт
     */
    @Column(name="performer_id")
    UUID performerId;

    /**
     * Описание
     */
    String description;

    /**
     * Цена
     */
    BigDecimal price;

    /**
     * Услуга доступна с
     */
    @Column(name = "start_time")
    LocalTime startTime;

    /**
     * Услуга доступна до
     */
    @Column(name = "end_time")
    LocalTime endTime;

    /**
     * Статус (DRAFT, ACTIVE, ARCHIVED)
     */
    @Enumerated(EnumType.STRING)
    ProductStatus status;

    /**
     * Признак удаленного продукта
     * Исключено из трансфера
     */
    @JsonIgnore
    Boolean deleted = false;

    /**
     * Признак активного продукта
     * Исключено из трансфера
     */
    @JsonIgnore
    Boolean active = true;

    @Version
    @JsonIgnore
    Long version;
}
