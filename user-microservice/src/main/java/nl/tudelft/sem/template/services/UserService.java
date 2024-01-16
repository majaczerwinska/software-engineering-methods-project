package nl.tudelft.sem.template.services;

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.EntityExistsException;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final transient UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     *
     * @param appUser - the user to be created
     * @return - the created user
     */
    public AppUser createUser(AppUser appUser) {
        // Check if the appUser is null or has missing required fields
        if (appUser == null || !appUser.getEmail().toString().contains("@")) {
            throw new IllegalArgumentException("Invalid user data");
        }

        if (userRepository.existsByEmail(appUser.getEmail())) {
            throw new EntityExistsException("User already exists");
        }

        return userRepository.save(appUser);
    }

    /**
     * Retrieves the user with the specified id.
     *
     * @param userId - id of a user
     * @return - user with this id if exists, else null
     */
    public AppUser getUserById(long userId) {
        if (userRepository.findById(userId).isPresent()) {
            return userRepository.findById(userId).get();
        }
        return null;
    }

    public boolean userExistsById(long userId) {
        return userRepository.existsById(userId);
    }

    /**
     * Retrieves a list of users with the specified name.
     *
     * @param firstName - first name
     * @param lastName - last name
     * @return - list of users with the specified name
     */
    public List<AppUser> getUserByName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(new Name(firstName), new Name(lastName));
    }


    /**
     * Retrieves the user with the specified email.
     *
     * @param email - email of a user
     * @return - user with this email if exists, else null
     */
    public AppUser getUserByEmail(Email email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get();
        }
        return null;
    }

    /**
     * Checks whether the user with the specified email exists.
     *
     * @param email - email of a user
     * @return - true if exists, false otherwise
     */
    public boolean userExistsByEmail(Email email) {
        if (getUserByEmail(email) == null) {

            return false;
        }
        return true;

    }

    /**
     * Deletes the user with the specified id.
     *
     * @param userId - id of the to be deleted user
     */
    public void deleteUser(long userId) throws IllegalArgumentException, NoSuchElementException {
        if (userId < 0) {
            throw new IllegalArgumentException("Invalid user data");
        }
        if (getUserById(userId) == null) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(userId);
    }

    /**
     * Updates an existing user account and saves it in the repository.
     *
     * @param updatedUser - the updated user account to be saved.
     * @return the updated user account that was saved.
     */
    public AppUser updateUser(AppUser updatedUser) {
        if (updatedUser == null) {
            throw new IllegalArgumentException("Invalid user data");
        }
        // If the id of the updatedUser does not correspond to an existing user,
        // throw an IllegalArgumentException.
        AppUser existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Update the user properties
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAffiliation(updatedUser.getAffiliation());
        existingUser.setCommunication(updatedUser.getCommunication());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLink(updatedUser.getLink());

        // Save the updated user to the database and return
        return userRepository.save(existingUser);
    }
}
