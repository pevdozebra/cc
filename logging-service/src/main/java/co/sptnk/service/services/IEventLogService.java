package co.sptnk.service.services;

import co.sptnk.service.model.EventLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface IEventLogService {

    Flux<EventLog> getAll(Map<String, Object> params);
    Mono<EventLog> put(EventLog eventLog);
}
