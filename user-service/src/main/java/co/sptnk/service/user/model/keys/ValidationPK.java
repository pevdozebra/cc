package co.sptnk.service.user.model.keys;

import co.sptnk.service.user.common.ValidationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ValidationPK implements Serializable {

    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    private ValidationType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationPK that = (ValidationPK) o;
        return this.getId().equals(that.getId()) &&
                this.getType().getName().equals(that.getType().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getType().getName());
    }
}
