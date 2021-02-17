package co.sptnk.service.services.impl;

import co.sptnk.lib.common.AMQPKeys;
import co.sptnk.lib.common.eventlog.EventLogDTO;
import co.sptnk.service.model.EventLog;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = AMQPKeys.QUEUE_EVENTLOG)
public class EventLogReceiver {

    private final ModelMapper modelMapper;

    private final EventLogService eventLogService;

    public EventLogReceiver(ModelMapper mapper, EventLogService service) {
        this.eventLogService = service;
        this.modelMapper = mapper;
    }

    @RabbitHandler
    public void receiveMessage(EventLogDTO eventlogDTO) {
        EventLog eventLog = modelMapper.map(eventlogDTO, EventLog.class);
        eventLogService.save(eventLog).subscribe(r -> {
            log.info("Объект " + r.getId() + " сохранен в БД");
        });
    }
}
