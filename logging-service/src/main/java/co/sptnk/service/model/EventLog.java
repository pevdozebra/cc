package co.sptnk.service.model;


import co.sptnk.service.keys.EventCode;
import co.sptnk.service.keys.EventType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EventLog")
    @SequenceGenerator(name = "EventLog", sequenceName = "event_log_id_seq")
    UUID id;

    @Column(name = "user_id")
    UUID userId;

    @Enumerated
    @Column(name = "event_type")
    EventType type;

    @Enumerated
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
}
