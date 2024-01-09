package nl.tudelft.sem.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.help.UserRepositoryTest;
import nl.tudelft.sem.template.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class UserServiceTest {
    private UserRepositoryTest userRepository;
    private UserService userService;
    private AppUser appUser;
    private Email email;

    /**
     * Setting up the environment.
     */
    @BeforeEach
    public void setup() {
        userRepository = new UserRepositoryTest();
        userService = new UserService(userRepository);
        Long id = 1L;
        email = new Email("abc@fun.org");
        Name name = new Name("user");
        UserAffiliation affiliation = new UserAffiliation("affiliation");
        Link link = new Link("link");
        Communication communication = new Communication("communication");
        appUser = new AppUser(id, email, name, affiliation, link, communication);
    }

    @Test
    public void createInvalidUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null);
        });
    }

    @Test
    public void createValidUser() {
        AppUser user = userService.createUser(appUser);

        userRepository.save(appUser);
        assertEquals(userRepository.findById(String.valueOf(1L)).get(), user);
    }

    @Test
    public void getInvalidUser() {
        assertNull(userService.getUserById(1L));
    }

    @Test
    public void getValidUser() {
        userRepository.save(appUser);
        assertEquals(userRepository.findById(String.valueOf(1L)).get(), userService.getUserById(1L));
    }

    @Test
    public void userNonexistent() {
        assertFalse(userService.userExistsByEmail(new Email("123@email.nl")));
    }

    @Test
    public void userExists() {
        userRepository.save(appUser);
        assertTrue(userService.userExistsByEmail(email));
    }

    @Test
    public void deleteInvalidUser() {
        userRepository.save(appUser);
        userService.deleteUserById(2L);
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    public void deleteValidUser() {
        userRepository.save(appUser);
        userService.deleteUserById(1L);
        assertEquals(0, userRepository.findAll().size());
    }
}
