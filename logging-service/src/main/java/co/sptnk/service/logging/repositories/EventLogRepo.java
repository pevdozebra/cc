package co.sptnk.service.logging.repositories;

import co.sptnk.service.logging.model.EventLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventLogRepo extends ReactiveMongoRepository<EventLog, UUID> {
}
