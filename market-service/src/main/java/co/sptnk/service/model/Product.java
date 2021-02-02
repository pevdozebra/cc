package co.sptnk.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
     */
    private Boolean deleted = false;

    /**
     * Признак активного продукта
     */
    private Boolean active = true;
}
