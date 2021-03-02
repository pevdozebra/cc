package co.sptnk.service.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "validation_code")
public class ValidationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ValidationCode")
    @SequenceGenerator(name = "ValidationCode", sequenceName = "validation_code_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Validation validation;

    private String value;

    @Column(name = "issue_date")
    private OffsetDateTime issueDate;

    @Column(name = "expire_date")
    private OffsetDateTime expireDate;
}
