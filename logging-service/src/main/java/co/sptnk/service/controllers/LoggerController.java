package co.sptnk.service.controllers;

import co.sptnk.service.model.EventLog;
import co.sptnk.service.services.impl.EventLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@RequestMapping("eventlog")
@RestController
public class LoggerController {

    @Autowired
    EventLogService service;

    /**
     * Метод получения всех запаисей логов по опциональному фильтру
     * @param map - возможные значения параметров фильтра
     * @return страницу объектов логов
     */
    @Operation(description = "Получение страницы объектов логирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Полученя список объектов"),
            @ApiResponse(responseCode = "400", description = "Неверно заданы параметры поиска")
    }
    )
    @GetMapping
    public Flux<EventLog> getAll(@RequestParam Map<String, String> map) {
        return service.getAll(map);
    }
}
