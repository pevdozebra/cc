package co.sptnk.service.user.common;

import co.sptnk.lib.common.AMQPKeys;
import co.sptnk.lib.common.eventlog.EventCode;
import co.sptnk.lib.common.eventlog.EventLogDTO;
import co.sptnk.lib.common.eventlog.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendLogMessage(EventCode code, EventType type, String description) {
        EventLogDTO eventLog = new EventLogDTO();
        eventLog.setCode(code);
        eventLog.setType(type);
        eventLog.setDescription(description);
        eventLog.setEventDate(LocalDateTime.now());
        rabbitTemplate.convertSendAndReceive(AMQPKeys.TOPIC_EVENTLOG, AMQPKeys.QUEUE_EVENTLOG, eventLog);
    }
}
