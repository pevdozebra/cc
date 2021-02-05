package co.sptnk.service.services.impl;

import co.sptnk.service.exceptions.LoggingServiceException;
import co.sptnk.service.model.EventLog;
import co.sptnk.service.repositories.EventLogRepo;
import co.sptnk.service.services.IEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EventLogService implements IEventLogService {

    @Autowired
    EventLogRepo eventLogRepo;

    @Override
    public EventLog add(EventLog eventLog) throws LoggingServiceException {
        if (eventLog.getId() != null) {
            throw new LoggingServiceException("Объект с таким ID уже существует");
        }
        return eventLogRepo.save(eventLog);
    }

    @Override
    public EventLog update(EventLog eventLog) throws LoggingServiceException {
        if (eventLog.getId() == null) {
            throw new LoggingServiceException("Невозможно идентифицировать сохраняемый объект");
        }
        EventLog exist = eventLogRepo.findById(eventLog.getId()).orElse(null);
        if (exist == null) {
            throw new LoggingServiceException("Объект для сохранения не найден");
        }
        return eventLogRepo.save(eventLog);
    }

    @Override
    public void delete(UUID uuid) throws LoggingServiceException {
        String error = "Нельзя удалить объекта лога с id " + uuid;
        throw new LoggingServiceException(error);
    }

    @Override
    public EventLog getOneById(UUID uuid) throws LoggingServiceException {
        EventLog order = eventLogRepo.findById(uuid).orElse(null);
        if (order == null) {
            throw new LoggingServiceException("Объект не найден");
        }
        return order;
    }

    @Override
    public List<EventLog> getAll(Map<String, String> params) {
        return eventLogRepo.findAll();
    }
}
