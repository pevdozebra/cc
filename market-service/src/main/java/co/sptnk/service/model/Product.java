package co.sptnk.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Product")
    @SequenceGenerator(name = "Product", sequenceName = "products_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Индектификатор пользователя, которому принадлежит продукт
     */
    @Column(name="user_id")
    private Long userId;

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
}
