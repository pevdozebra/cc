package co.sptnk.service.services;

import co.sptnk.service.model.EventLog;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface IEventLogService {

    Flux<EventLog> getAll(Map<String, String> params);
}
