package co.sptnk.service.controllers;

import co.sptnk.lib.base.AbstractCHController;
import co.sptnk.lib.exceptions.ServiceException;
import co.sptnk.lib.keys.AllowedLinksMethods;
import co.sptnk.service.model.EventLog;
import co.sptnk.service.services.impl.EventLogService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;



@OpenAPIDefinition(info = @Info(description = "REST API сервиса логирования", title = "Logging-service"))
@Tag(name = "EventLogController", description = "API для объекта EventLog")
@RequestMapping("eventlog")
@RestController
public class LoggerController extends AbstractCHController<EventLog, UUID> {

    @Autowired
    EventLogService service;

    @Override
    public ResponseEntity<EventLog> getOneById(@PathVariable("id") UUID uuid) {
        EventLog entity;
        try {
            entity = createLinks(service.getOneById(uuid), uuid, AllowedLinksMethods.GET);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventLog> add(@RequestBody EventLog eventLog) {
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
    public ResponseEntity<EventLog> update(@RequestBody EventLog eventLog) {
        EventLog result;
        try {
            result = createLinks(service.update(eventLog), eventLog.getId(), AllowedLinksMethods.PUT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventLog> delete(@PathVariable("id") UUID uuid) {
        try {
            service.delete(uuid);
        } catch (ServiceException e) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<?> getAll(Map<String, String> map) {
        return new ResponseEntity<>(service.getAll(map), HttpStatus.OK);
    }

    @Override
    protected Class<? extends AbstractCHController<EventLog, UUID>> getSelfClass() {
        return this.getClass();
    }
}
