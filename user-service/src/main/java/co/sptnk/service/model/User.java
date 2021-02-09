package co.sptnk.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="users")
public class User extends RepresentationModel<User> {

    /**
     * Индектификатор пользователя
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Имя
     */
    @Column(name = "firstname")
    private String firstname;

    /**
     * Фамилия
     */
    @Column(name = "lastname")
    private String lastname;

    /**
     * Логин
     */
    @Column(name = "username")
    private String username;

    /**
     * Пароль
     */
    @Column(name = "password")
    private String password;

    /**
     * Дата рождения
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Флаг блокировки
     */
    @Column(name = "blocked")
    private Boolean blocked = false;

    /**
     * Флаг удаления записи (фактическое удаление не происходит)
     */
    @Column(name = "deleted")
    @JsonIgnore
    private Boolean deleted = false;


    @ManyToMany
    @JoinTable (name="rel_user_interest",
            joinColumns=@JoinColumn (name="user_id"),
            inverseJoinColumns=@JoinColumn(name="interest_id"))
    private List<Interest> interests;

//    @OneToMany(mappedBy = "rated", fetch=FetchType.LAZY)
//    public List<PerformerRating> ratings;
//
//    @OneToMany(mappedBy = "rater", fetch=FetchType.LAZY)
//    public List<PerformerRating> postedRatings;
//
//    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
//    public List<Card> cards;
//
//    @OneToOne(mappedBy = "performer", fetch = FetchType.LAZY)
//    private PerformerVerification verification;
//
//    @OneToMany(mappedBy = "verifier", fetch=FetchType.LAZY)
//    public List<PerformerVerification> postedVerifications;

}