package co.sptnk.service.model;


import co.sptnk.lib.keys.EventCode;
import co.sptnk.lib.keys.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "event_log")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventLog {

    @Id
    UUID id;

    UUID userId;

    EventType type;

    EventCode code;

    LocalDateTime eventDate;

    String clientType;

    String clientIp;

    String userAgent;

    String description;

    @Version
    @JsonIgnore
    Long version;
}
