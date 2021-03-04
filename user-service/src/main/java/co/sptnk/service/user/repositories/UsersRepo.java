package co.sptnk.service.user.repositories;

import co.sptnk.service.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepo extends JpaRepository<User, UUID> {

    Collection<User> findAllByDeletedFalse();

    Optional<User> findUserByIdAndDeletedFalse(UUID id);

    Optional<User> findUserByUsername(String username);
}
