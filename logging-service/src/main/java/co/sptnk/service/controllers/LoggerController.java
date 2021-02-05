package co.sptnk.service.controllers;

import co.sptnk.lib.base.AbstractCHController;
import co.sptnk.lib.exceptions.ServiceException;
import co.sptnk.lib.keys.AllowedLinksMethods;
import co.sptnk.service.keys.EventCode;
import co.sptnk.service.model.EventLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.UUID;

public class LoggerController extends AbstractCHController<EventLog, UUID> {
    @Override
    public ResponseEntity<EventLog> getOneById(@PathVariable("id") UUID uuid) {
        return null;
    }

    @Override
    public ResponseEntity<EventLog> add(EventLog eventLog) {
        EventLog result;
        try {
            result = service.add(eventLog);
            result = createLinks(result, result.getId(), AllowedLinksMethods.POST);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EventLog> update(EventLog eventLog) {
        return null;
    }

    @Override
    public ResponseEntity<EventLog> delete(@PathVariable("id") UUID uuid) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAll(Map<String, String> map) {
        return null;
    }

    @Override
    protected Class<? extends AbstractCHController<EventLog, UUID>> getSelfClass() {
        return this.getClass();
    }
}
