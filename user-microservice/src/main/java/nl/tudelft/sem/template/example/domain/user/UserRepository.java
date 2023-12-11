package nl.tudelft.sem.template.example.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    /**
     * Find user by NetID.
     */
    Optional<AppUser> findByEmail(Email email);

    /**
     * Check if an existing user already uses a NetID.
     */
    boolean existsByEmail(Email email);

    @Override
    AppUser save(AppUser appUser);
}
