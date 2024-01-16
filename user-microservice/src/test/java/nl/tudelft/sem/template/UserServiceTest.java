package nl.tudelft.sem.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import javax.persistence.EntityExistsException;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        appUser = new AppUser(id, email, name, name, affiliation, link, communication);
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
        if (userRepository.findById(1L).isPresent()) {
            assertEquals(userRepository.findById(1L).get(), user);
        } else {
            assertEquals(0, 1);
        }

    }

    @Test
    public void createExistingUser() {
        userRepository.save(appUser);
        AppUser appUser1 = new AppUser(
                new Email("abc@fun.org"),
                new Name("user1"),
                new Name("surname"),
                new UserAffiliation("affiliation"),
                new Link("url"),
                new Communication("communication")
        );
        assertThrows(EntityExistsException.class, () -> {
            userService.createUser(appUser1);
        });

    }

    @Test
    public void getInvalidUser() {
        assertNull(userService.getUserById(1L));
    }

    @Test
    public void getValidUser() {
        userRepository.save(appUser);
        if (userRepository.findById(1L).isPresent()) {
            assertEquals(userRepository.findById(1L).get(), userService.getUserById(1L));
        } else {
            assertEquals(0, 1);
        }

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
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(-1L);
        });
    }

    @Test
    public void deleteValidUser() {
        userRepository.save(appUser);
        userService.deleteUser(1L);
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void deleteUserNonExistent() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.deleteUser(appUser.getId());
        });
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
        assertEquals(1, userService.getUserByName("user", "user").size());
    }

    @Test
    public void getInvalidUserByName() {
        assertEquals(0, userService.getUserByName("John", "Johnson").size());
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
    public void updateNonExistentUser() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.updateUser(appUser);
        });
    }

    @Test
    public void updateValidUser() {
        userRepository.save(appUser);
        appUser.setEmail(new Email("abc@gmail.com"));
        appUser.setFirstName(new Name("mark"));
        appUser.setLastName(new Name("smith"));
        appUser.setAffiliation(new UserAffiliation("police"));
        appUser.setLink(new Link("url"));
        appUser.setCommunication(new Communication("phone"));

        assertEquals(appUser, userService.updateUser(appUser));
        assertEquals(appUser.getEmail().toString(), "abc@gmail.com");
        assertEquals(appUser.getFirstName().toString(), "mark");
        assertEquals(appUser.getLastName().toString(), "smith");
        assertEquals(appUser.getAffiliation().toString(), "police");
        assertEquals(appUser.getLink().toString(), "url");
        assertEquals(appUser.getCommunication().toString(), "phone");
    }


}
