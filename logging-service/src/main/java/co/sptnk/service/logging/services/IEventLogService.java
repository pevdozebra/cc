package co.sptnk.service.logging.services;

import co.sptnk.service.logging.model.EventLog;

import java.util.List;
import java.util.Map;

public interface IEventLogService {

    List<EventLog> getAll(Map<String, String> params);
    EventLog save(EventLog eventLog);
}
