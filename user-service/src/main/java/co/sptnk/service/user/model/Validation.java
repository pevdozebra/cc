package co.sptnk.service.user.model;

import co.sptnk.service.user.model.keys.ValidationPK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "validation")
public class Validation {

    @EmbeddedId
    private ValidationPK id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "send_count")
    private Integer sendCount;

    @Column(name = "first_send_date")
    private OffsetDateTime lastSendDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "validation_id"),
            @JoinColumn(name = "validation_type")
    })
    private Set<ValidationCode> codes = new HashSet<>();

    public void addCode(ValidationCode code) {
        codes.add(code);
    }

    public void deleteCode(ValidationCode code) {
        codes.remove(code);
    }
}
