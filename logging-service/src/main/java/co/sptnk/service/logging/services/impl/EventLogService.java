package co.sptnk.service.logging.services.impl;

import co.sptnk.service.logging.model.EventLog;
import co.sptnk.service.logging.repositories.EventLogRepo;
import co.sptnk.service.logging.services.IEventLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EventLogService implements IEventLogService {

    @Autowired
    EventLogRepo eventLogRepo;


    @Override
    public List<EventLog> getAll(Map<String, String> params) {
        return eventLogRepo.findAll();
    }

    @Override
    public EventLog save(EventLog eventLog) {
        eventLog.setId(UUID.randomUUID());
        if( eventLog.getEventDate() == null)
            eventLog.setEventDate(LocalDateTime.now());
        return eventLogRepo.save(eventLog);
    }

}
