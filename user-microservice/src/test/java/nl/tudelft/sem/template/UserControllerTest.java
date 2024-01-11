package nl.tudelft.sem.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.template.controllers.UserController;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.FirstName;
import nl.tudelft.sem.template.domain.user.LastName;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.help.UserRepositoryTest;
import nl.tudelft.sem.template.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class UserControllerTest {

    private UserService userService;
    private  UserRepositoryTest userRepository;
    private UserController userController;

    /**
     * Setting up the environment.
     */
    @BeforeEach
    public void setup() {
        userRepository = new UserRepositoryTest();
        userService = new UserService(userRepository);
        userController = new UserController(userService);
    }

    /**
     * Test for getting an invalid id.
     */
    @Test
    public void getUserByIdInvalidId() {
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(), userController.getAccountByID(-1));
    }

    /**
     * Test for an unauthorized user.
     */
    @Test
    public void getUserByIdUnauthorized() {
        //TODO: !!!
    }

    /**
     * Test for a nonexistent user.
     */
    @Test
    public void getUserByIdUserNonexistent() {
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), userController.getAccountByID(0));
    }

    /**
     * Test for a valid user.
     */
    @Test
    public void getUserByIdUserExists() {
        Email email = new Email("abc@cool.com");
        FirstName firstName = new FirstName("test");
        LastName lastName = new LastName("test");
        UserAffiliation userAffiliation = new UserAffiliation("test");
        Link link = new Link("test");
        Communication com = new Communication("communicateMe");
        AppUser user = new AppUser(1L, email, firstName, lastName, userAffiliation, link, com);
        userRepository.save(user);
        AppUser appUser = userRepository.findById(String.valueOf(1L)).get();
        int id = (int) 1L;
        assertEquals(appUser.toModelUser(), userController.getAccountByID((Integer) id).getBody());
    }

}
