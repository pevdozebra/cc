package co.sptnk.lib.utils;

import co.sptnk.lib.keys.EventCode;
import co.sptnk.lib.keys.EventType;
import co.sptnk.lib.model.EventLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class LogUtils {

    private static final String URL = "";

    public static ResponseEntity<EventLog> logger(EventLog eventLog) {
        if (eventLog.getEventDate() == null)
            eventLog.setEventDate(LocalDateTime.now());
        return new RestTemplate().postForEntity(URL, eventLog, EventLog.class);
    }

    public static ResponseEntity<EventLog> info(EventCode code) {
        EventLog eventLog = new EventLog();
        eventLog.setType(EventType.INFO);
        eventLog.setCode(code);
        eventLog.setEventDate(LocalDateTime.now());
        return new RestTemplate().postForEntity(URL, eventLog, EventLog.class);
    }

    public static ResponseEntity<EventLog> error(EventCode code) {
        EventLog eventLog = new EventLog();
        eventLog.setType(EventType.ERROR);
        eventLog.setCode(code);
        eventLog.setEventDate(LocalDateTime.now());
        return new RestTemplate().postForEntity(URL, eventLog, EventLog.class);
    }
}
