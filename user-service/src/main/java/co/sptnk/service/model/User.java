package co.sptnk.service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
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
@DynamicInsert
@DynamicUpdate
public class User extends RepresentationModel<User> {
    /**
     * Индектификатор пользователя
     */
    @Id
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
     * Дата рождения
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * email
     */
    @Column(name = "email")
    private String email;

    /**
     * Флаг блокировки
     */
    @Column(name = "blocked")
    private Boolean blocked;

    /**
     * Флаг удаления записи (фактическое удаление не происходит)
     */
    @Column(name = "deleted")
    private Boolean deleted;


    @ManyToMany
    @JoinTable (name="rel_user_interest",
            joinColumns=@JoinColumn (name="user_id"),
            inverseJoinColumns=@JoinColumn(name="interest_id"))
    private List<Interest> interests;

    public User(UserRepresentation userRepresentation) {
        this.id =  UUID.fromString(userRepresentation.getId());
        this.firstname = userRepresentation.getFirstName();
        this.lastname = userRepresentation.getLastName();
        this.username = userRepresentation.getUsername();
        this.email = userRepresentation.getEmail();
        this.blocked = !userRepresentation.isEnabled();
    }
}