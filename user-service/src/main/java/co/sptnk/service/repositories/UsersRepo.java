package co.sptnk.service.repositories;

import co.sptnk.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepo extends JpaRepository<User, UUID> {

    Collection<User> findAllByDeletedFalse();

    Optional<User> findUserByIdAndDeletedFalse(UUID id);

}
