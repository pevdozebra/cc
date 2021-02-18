package co.sptnk.service.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="performer_verification")
@DynamicInsert
@DynamicUpdate
public class PerformerVerification extends RepresentationModel<PerformerVerification> {
    /**
     * Индектификатор верификации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PerformerVerification")
    @SequenceGenerator(name = "PerformerVerification", sequenceName = "performer_verification_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Пользователь(только для исполнителей)
     */

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User performer;

    /**
     * Проверяющий
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User verifier;

    /**
     * Дата заполнения
     */
    @Column(name = "create_date")
    private LocalDateTime createDate;

    /**
     * Дата решения
     */
    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

    /**
     * 	Результат решения
     */
    @Column(name = "success_decision")
    private Boolean successDecision;

    /**
     * Комментарий
     */
    @Column(name = "comment")
    private String comment;

    /**
     * Флаг удаления записи (фактическое удаление не происходит)
     */
    @Column(name = "deleted")
    private Boolean deleted;
}
