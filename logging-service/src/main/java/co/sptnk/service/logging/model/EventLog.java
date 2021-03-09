package co.sptnk.service.logging.model;


import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventlog")
public class EventLog {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    /**
     * Идентификатор пользователя
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Тип события
     */
    @Enumerated(EnumType.STRING)
    private EventType type;

    /**
     * Код события
     */
    @Enumerated(EnumType.STRING)
    private EventCode code;

    /**
     * Дата события
     */
    @Column(name = "event_date")
    private OffsetDateTime eventDate;

    /**
     * Тип клиента
     */
    @Column(name = "client_type")
    private String clientType;

    /**
     * IP пользователя
     */
    @Column(name = "client_ip")
    private String clientIp;

    /**
     * User agent пользователя
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * Описание события
     */
    private String description;

    @Version
    @JsonIgnore
    private Integer version;
}
