package co.sptnk.service.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_role")
@DynamicInsert
@DynamicUpdate
public class Role {
    public static final String CUSTOMER = "customer";
    public static final String PERFORMER = "performer";
    public static final String MANAGER = "manager";
    public static final String ADMIN = "admin";
    public static final String ANONYMOUS = "anonymousUser";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Role")
    @SequenceGenerator(name = "Role", sequenceName = "user_role_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private User user;

    private String name;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getName()!= null ? this.getName().hashCode() : 0);
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
        Role entity = (Role) other;
        if (this.getName() == null) {
            return false;
        }
        if (entity.getName() == null) {
            return false;
        }
        return this.getName().equals(entity.getName());
    }
}
