package co.sptnk.service.logging.controllers;

import co.sptnk.service.logging.services.impl.EventLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RequestMapping("eventlog")
@Tag(name = "LoggerController", description = "API объекта EventLog")
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
    public ResponseEntity<?> getAll(@RequestParam Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }
}
