package nl.tudelft.sem.template.domain.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    /**
     * Finds users by Email.

     * @param email - Email to find a User with
     * @return Optional<AppUser> which is empty if the AppUser was not found </AppUser>
     */
    Optional<AppUser> findByEmail(Email email);

    /**
     * Finds a list of users with a matching first name and last name.
     *
     * @param firstName - first name
     * @param lastName - last name
     * @return - list of users with matching names
     */
    List<AppUser> findByName(FirstName firstName, LastName lastName);

    /**
     * Checks if User exists by Email.

     * @param email - Use the specified Email to check if a User exists with this Email
     * @return true if User exists
     */
    boolean existsByEmail(Email email);
}
