package co.sptnk.service.logging.services.impl;

import co.sptnk.service.logging.model.EventLog;
import co.sptnk.service.logging.repositories.EventLogRepo;
import co.sptnk.service.logging.services.IEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;


@Service
public class EventLogService implements IEventLogService {

    @Autowired
    EventLogRepo eventLogRepo;


    @Override
    public Flux<EventLog> getAll(Map<String, String> params) {
        return eventLogRepo.findAll();
    }

}
