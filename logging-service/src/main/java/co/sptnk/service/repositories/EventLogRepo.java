package co.sptnk.service.repositories;

import co.sptnk.lib.keys.EventCode;
import co.sptnk.lib.keys.EventType;
import co.sptnk.service.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface EventLogRepo extends JpaRepository<EventLog, UUID> {

    Collection<EventLog> findAllByTypeAndCodeAndEventDate(EventType type, EventCode code, LocalDateTime date);
}
