package co.sptnk.service.model;


import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event_log")
public class EventLog {

    @Id
    private UUID id;

    private UUID userId;

    private EventType type;

    private EventCode code;

    private LocalDateTime eventDate;

    private String clientType;

    private String clientIp;

    private String userAgent;

    private String description;
}
