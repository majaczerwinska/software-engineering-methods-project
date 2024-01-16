package nl.tudelft.sem.template.services;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.EmailAlreadyInUseException;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.domain.user.UserRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for registering a new user.
 */
@Service
public class RegistrationService {
    private final transient UserRepository userRepository;

    /**
     * Instantiates a new UserService.
     *
     * @param userRepository  the user repository
     */
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Register a new user.
     *
     * @param email    The NetID of the user
     * @throws Exception if the user already exists
     */
    public AppUser registerUser(Email email, Name firstName, Name lastName,
                                UserAffiliation affiliation, Link link, Communication communication) throws Exception {

        if (checkEmailIsUnique(email)) {

            // Create new account
            AppUser user = new AppUser(1L, email, firstName, lastName, affiliation, link, communication);
            userRepository.save(user);

            return user;
        }

        throw new EmailAlreadyInUseException(email);
    }

    public boolean checkEmailIsUnique(Email email) {
        return !userRepository.existsByEmail(email);
    }
}
