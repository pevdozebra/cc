package co.sptnk.lib.common.eventlog;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventLogDTO implements Serializable {

    private static final long serialVersionUID = 2193385245346750259L;

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
