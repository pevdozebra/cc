package co.sptnk.service.repositories;

import co.sptnk.service.model.EventLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface EventLogRepo extends ReactiveMongoRepository<EventLog, UUID> {
}
