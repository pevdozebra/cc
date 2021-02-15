package co.sptnk.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.ManyToOne;
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
@Table(name="card")
public class Card extends RepresentationModel<Card> {
    /**
     * Индектификатор карты
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Card")
    @SequenceGenerator(name = "Card", sequenceName = "card_id_seq", allocationSize = 1)
    private Long id;

    /**
     * пользователь
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Дата привязки
     */
    @Column(name = "bind_date")
    private LocalDateTime bindDate;

    /**
     * Маскированный номер карты
     */
    @Column(name = "masked_number")
    private String maskedNumber;

    /**
     * Ссылка в системе эквайера
     */
    @Column(name = "aquire_url")
    private String aquireUrl;

    /**
     * Флаг архивной карты
     */
    @Column(name = "archived")
    private Boolean archived = false;

    /**
     * Флаг удаления записи (фактическое удаление не происходит)
     */
    @Column(name = "deleted")
    @JsonIgnore
    private Boolean deleted = false;
}
