package co.sptnk.service.services.impl;

import co.sptnk.service.model.EventLog;
import co.sptnk.service.repositories.EventLogRepo;
import co.sptnk.service.services.IEventLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EventLogService implements IEventLogService {

    @Autowired
    EventLogRepo eventLogRepo;


    @Override
    public Flux<EventLog> getAll(Map<String, String> params) {
        return eventLogRepo.findAll();
    }

    @Override
    public Mono<EventLog> save(EventLog eventLog) {
        return Mono.just(eventLog).flatMap(event -> {
            event.setId(UUID.randomUUID());
            if( eventLog.getEventDate() == null)
                eventLog.setEventDate(LocalDateTime.now());
            return eventLogRepo.save(eventLog);
        });
    }

}
