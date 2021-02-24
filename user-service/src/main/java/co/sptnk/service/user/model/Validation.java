package co.sptnk.service.user.model;

import co.sptnk.service.user.model.keys.ValidationPK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "validation")
public class Validation {

    @EmbeddedId
    private ValidationPK id;

    @Column(name = "send_count")
    private Integer sendCount;

    @Column(name = "first_send_date")
    private OffsetDateTime lastSendDate;

}
