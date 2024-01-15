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
    Optional<AppUser> findById(Long id);

    /**
     * Finds users by Email.

     * @param email - Email to find a User with
     * @return Optional<AppUser> which is empty if the AppUser was not found </AppUser>
     */
    Optional<AppUser> findByEmail(Email email);

    /**
     * Find user by Name.
     *
     * @param firstName - first name to find a User with
     * @param lastName - last name to find a User with
     * @return a List<AppUser> that match the specified Name </AppUser>
     */
    List<AppUser> findByFirstNameAndLastName(Name firstName, Name lastName);

    /**
     * Find users with matching first name.
     *
     * @param firstName - first name
     * @return - list of users
     */
    List<AppUser> findByFirstName(Name firstName);

    /**
     * Checks if User exists by Email.

     * @param email - Use the specified Email to check if a User exists with this Email
     * @return true if User exists
     */
    boolean existsByEmail(Email email);
}
