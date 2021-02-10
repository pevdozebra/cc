package co.sptnk.service.controllers;

import co.sptnk.service.model.EventLog;
import co.sptnk.service.services.impl.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;


@RequestMapping("eventlog")
@RestController
public class LoggerController {

    @Autowired
    EventLogService service;

    @PostMapping
    public Mono<EventLog> put(@RequestBody EventLog eventLog) {
        return service.put(eventLog);
    }

    @GetMapping
    public Flux<EventLog> getAll(Map<String, Object> map) {
        return service.getAll(map);
    }
}
