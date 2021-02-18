package co.sptnk.service.logging.services;

import co.sptnk.service.logging.model.EventLog;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface IEventLogService {

    Flux<EventLog> getAll(Map<String, String> params);
}
