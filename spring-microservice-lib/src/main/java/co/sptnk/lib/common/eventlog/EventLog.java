package co.sptnk.lib.common.eventlog;


import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EventLog {

    UUID id;

    UUID userId;

    EventType type;

    EventCode code;

    LocalDateTime eventDate;

    String clientType;

    String clientIp;

    String userAgent;

    String description;
}
