package co.sptnk.service.logging.search;

import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventType;
import co.sptnk.service.logging.model.EventLog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

/**
 * Класс для приведения параметров запроса getAll к объекту и получения объекта сортировки для запроса к БД
 */
@Getter
@Setter
public class EventLogSortData {

    private Example<EventLog> sample;
    private Pageable pageable;

    private EventLogSortData() {
    }

    public static EventLogSortData parse(Map<String, String> params) {
        EventLogSortData data = new EventLogSortData();
        try {
            EventLog eventLog = new EventLog();
            eventLog.setUserId(params.get("userId") != null ? UUID.fromString(params.get("userId")) : null);
//            this.startDate = params.get("startDate") != null ?
//                    LocalDateTime.parse(params.get("startDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
//            this.startDate = params.get("endDate") != null ?
//                    LocalDateTime.parse(params.get("endDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
            data.setPageable(PageRequest.of(
                    params.get("page") != null ? Integer.parseInt(params.get("page")) : 0,
                    params.get("count") != null ? Integer.parseInt(params.get("count")) : Integer.MAX_VALUE
            ));
            eventLog.setType(params.get("type") != null ? EventType.valueOf(params.get("type")) : null);
            eventLog.setCode(params.get("code") != null ? EventCode.valueOf(params.get("code")) : null);
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            data.setSample(Example.of(eventLog, matcher));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return data;
    }
}
