package nl.tudelft.sem.template.domain.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final transient UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the user with the specified id.

     * @param userId - id of a user
     * @return - user with this id if exists, else null
     */
    public AppUser getUserById(int userId) {
        if (userRepository.findById(String.valueOf(userId)).isPresent()) {
            return userRepository.findById(String.valueOf(userId)).get();
        }
        return null;
    }

    /**
     * Retrieves the user with the specified email.

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

     * @param userId - id of the to be deleted user
     */
    public void deleteUserById(int userId) {
        if (getUserById(userId) == null) {
            return;
        }
        userRepository.delete(getUserById(userId));
    }
}
