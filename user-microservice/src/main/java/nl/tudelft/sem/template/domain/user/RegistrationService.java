package nl.tudelft.sem.template.domain.user;

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
    public AppUser registerUser(Email email,
                                Name name, UserAffiliation affiliation,
                                Link link, Communication communication) throws Exception {

        if (checkEmailIsUnique(email)) {

            // Create new account
            AppUser user = new AppUser(email, name, affiliation, link, communication);
            userRepository.save(user);

            return user;
        }

        throw new EmailAlreadyInUseException(email);
    }

    public boolean checkEmailIsUnique(Email email) {
        return !userRepository.existsByEmail(email);
    }
}
