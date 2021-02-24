package co.sptnk.service.user.model;

import co.sptnk.service.user.common.config.ParamType;
import co.sptnk.service.user.common.config.ConfigName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "service_configuration")
public class ServiceConfiguration {

    @Id
    @Enumerated(EnumType.STRING)
    private ConfigName id;

    private String description;

    @Column(name = "value_type")
    @Enumerated(EnumType.STRING)
    private ParamType valueType;

    private String value;
}
