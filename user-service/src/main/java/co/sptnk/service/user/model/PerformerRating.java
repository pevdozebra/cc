package co.sptnk.service.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="performer_rating")
@DynamicInsert
@DynamicUpdate
public class PerformerRating extends RepresentationModel<PerformerRating> {

    /**
     * Индектификатор оценки исполнителя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PerformerRating")
    @SequenceGenerator(name = "PerformerRating", sequenceName = "performer_rating_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Исполнитель
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id")
    private User rated;

    /**
     * заказчик
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User rater;

    /**
     * причина (id заказа)
     */
    @Column(name = "reason_id")
    private Long reasonId;

    /**
     * Оценка
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * Комментарий
     */
    @Column(name = "comment")
    private String comment;

    /**
     * Дата оценки
     */
    @Column(name = "rating_timestamp")
    private OffsetDateTime date;

    /**
     * Флаг удаления записи (фактическое удаление не происходит)
     */
    @Column(name = "deleted")
    private Boolean deleted;

}
