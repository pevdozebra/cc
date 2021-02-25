package co.sptnk.service.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="interest")
@DynamicInsert
@DynamicUpdate
public class Interest extends RepresentationModel<Interest> {
    /**
     * Индектификатор интереса
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Interest")
    @SequenceGenerator(name = "Interest", sequenceName = "interest_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Имя
     */
    @Column(name = "title")
    private String title;

    /**
     * Ссылка на родителя
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Interest parent;

    /**
     * Флаг удаления записи (фактическое удаление не происходит)
     */
    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToMany
    @JoinTable (name="rel_user_interest",
            joinColumns=@JoinColumn (name="interest_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    @JsonIgnore
    private Set<User> users;

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
        Interest entity = (Interest) other;
        if (this.getId() == null) {
            return false;
        }
        if (entity.getId() == null) {
            return false;
        }
        return this.getId().equals(entity.getId());
    }

}
