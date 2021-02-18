package co.sptnk.service.logging.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

/**
 * Класс для приведения параметров запроса getAll к объекту и получения объекта сортировки для запроса к БД
 */
@Getter
@Setter
@NoArgsConstructor
public class EventLogSortData {
    private UUID userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String type;
    private String code;
    private Long offset;
    private Long pageNum;

    public void parse(Map<String, String> params) {
        try {
            this.userId = params.get("userId") != null ? UUID.fromString(params.get("userId")) : null;
            this.startDate = params.get("startDate") != null ?
                    LocalDateTime.parse(params.get("startDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
            this.startDate = params.get("endDate") != null ?
                    LocalDateTime.parse(params.get("endDate"), DateTimeFormatter.ISO_DATE_TIME) : null;
            this.offset = params.get("offset") != null ? Long.parseLong(params.get("offset")) : null;
            this.pageNum = params.get("page") != null ? Long.parseLong(params.get("page")) : null;
            this.type = params.get("type") != null ? params.get("type") : null;
            this.code = params.get("code") != null ? params.get("code") : null;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
 }
}
