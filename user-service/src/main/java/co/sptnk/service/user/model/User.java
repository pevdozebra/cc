package co.sptnk.service.user.model;

import co.sptnk.service.user.model.dto.UserSignUpData;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserDetails userDetails;
    /**
     * Имя
     */
    @Column(name = "firstname")
    private String firstName;

    /**
     * Фамилия
     */
    @Column(name = "lastname")
    private String lastName;

    /**
     * Логин(телефон)
     */
    @Column(name = "username", unique = true)
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
    private Set<Interest> interests;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name ="user_id")
    @JsonManagedReference
    private Set<Role> roles;

    public User(UserResource userResource) {
        UserRepresentation userRepresentation = userResource.toRepresentation();
        this.id =  UUID.fromString(userRepresentation.getId());
        this.firstName = userRepresentation.getFirstName();
        this.lastName = userRepresentation.getLastName();
        this.username = userRepresentation.getUsername();
        this.email = userRepresentation.getEmail();
        this.blocked = !userRepresentation.isEnabled();
    }

    public User(UserSignUpData userSignUpData) {
        this.firstName = userSignUpData.getFirstName();
        this.lastName = userSignUpData.getLastName();
        this.username = userSignUpData.getPhone();
        this.email = userSignUpData.getEmail();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId()!= null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other.getClass() == getClass())) {
            return false;
        }
        User entity = (User) other;
        if (this.getId() == null) {
            return false;
        }
        if (entity.getId() == null) {
            return false;
        }
        return this.getId().equals(entity.getId());
    }

    public Set<Role> getRolesByName(Set<String> givenRoles) {
        return this.getRoles().stream().filter(r->givenRoles.contains(r.getName())).collect(Collectors.toSet());
    }
}
