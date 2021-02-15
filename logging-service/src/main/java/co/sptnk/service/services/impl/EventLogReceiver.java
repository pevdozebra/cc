package co.sptnk.service.services.impl;

import co.sptnk.lib.common.AMQPKeys;
import co.sptnk.lib.common.eventlog.EventLogDTO;
import co.sptnk.service.model.EventLog;
import co.sptnk.service.repositories.EventLogRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class EventLogReceiver {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventLogRepo eventLogRepo;

    @RabbitListener(queues = AMQPKeys.QUEUE_EVENTLOG)
    public void receiveMessage(EventLogDTO eventlogDTO) {
        EventLog eventLog = modelMapper.map(eventlogDTO, EventLog.class);
        eventLog.setId(UUID.randomUUID());
        if( eventLog.getEventDate() == null)
            eventLog.setEventDate(LocalDateTime.now());
        eventLogRepo.save(eventLog);
    }
}
