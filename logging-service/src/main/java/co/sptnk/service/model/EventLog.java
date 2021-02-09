package co.sptnk.service.model;


import co.sptnk.lib.keys.EventCode;
import co.sptnk.lib.keys.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "event_log")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventLog extends RepresentationModel<EventLog> {

    @Id
    @GeneratedValue(generator = "EventLogUUID")
    @GenericGenerator(
            name = "EventLogUUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false)
    UUID id;

    @Column(name = "user_id")
    UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    EventType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_code")
    EventCode code;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "client_type")
    String clientType;

    @Column(name = "client_ip")
    String clientIp;

    @Column(name = "user_agent")
    String userAgent;

    String description;

    @Version
    @JsonIgnore
    Long version;
}
