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

    @Test
    public void userNonexistantById() {
        assertFalse(userService.userExistsById(1L));
    }

    @Test
    public void userExistsById() {
        userRepository.save(appUser);
        assertTrue(userService.userExistsById(1L));
    }

    @Test
    public void getValidUserByName() {
        userRepository.save(appUser);
        assertEquals(1, userService.getUserByName("user").size());
    }

    @Test
    public void getInvalidUserByName() {
        assertEquals(0, userService.getUserByName("user").size());
    }

    @Test
    public void getValidUserByEmail() {
        userRepository.save(appUser);
        assertEquals(appUser, userService.getUserByEmail(new Email("abc@fun.org")));
    }

    @Test
    public void getInvalidUserByEmail() {
        assertNull(userService.getUserByEmail(new Email("abc@fun.org")));
    }

    @Test
    public void updateNullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null);
        });
    }

    @Test
    public void updateInvalidUser() {
        AppUser appUser1 = new AppUser(-1L,
                new Email("abc@gmail.com"),
                new Name("mark"),
                new UserAffiliation("police"),
                new Link("url"),
                new Communication("phone"));
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(appUser1);
        });
    }

    @Test
    public void updateNonExistentUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(appUser);
        });
    }

    @Test
    public void updateValidUser() {
        userRepository.save(appUser);
        AppUser appUser1 = new AppUser(1L,
                new Email("abc@gmail.com"),
                new Name("mark"),
                new UserAffiliation("police"),
                new Link("url"),
                new Communication("phone"));
        assertEquals(appUser1, userService.updateUser(appUser1));
        assertEquals(appUser.getEmail().toString(), "abc@gmail.com");
        assertEquals(appUser.getName().toString(), "mark");
        assertEquals(appUser.getAffiliation().toString(), "police");
        assertEquals(appUser.getLink().toString(), "url");
        assertEquals(appUser.getCommunication().toString(), "phone");
    }


}
