package co.sptnk.service.controllers;

import co.sptnk.service.model.EventLog;
import co.sptnk.service.services.impl.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;


@RequestMapping("eventlog")
@RestController
public class LoggerController {

    @Autowired
    EventLogService service;

    @GetMapping
    public Flux<EventLog> getAll(@RequestParam Map<String, String> map) {
        return service.getAll(map);
    }
}
