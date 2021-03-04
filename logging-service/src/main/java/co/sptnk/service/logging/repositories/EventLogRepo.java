package co.sptnk.service.logging.repositories;

import co.sptnk.service.logging.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventLogRepo extends JpaRepository<EventLog, UUID> {
}
