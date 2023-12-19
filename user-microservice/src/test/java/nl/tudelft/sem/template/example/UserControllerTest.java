package nl.tudelft.sem.template.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.domain.user.UserService;
import nl.tudelft.sem.template.example.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class UserControllerTest {

    private UserService userService;
    private UserRepositoryTest userRepository;
    private UserController userController;

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
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(), userController.getUserById(-1));
    }

    /**
     * Test for an unauthorized user.
     */
    @Test
    public void getUserByIdUnauthorized() {
        //TODO: !!!
    }

    /**
     * Test for a nonexistent user
     */
    @Test
    public void getUserByIdUserNonexistent() {
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), userController.getUserById(0));
    }

    /**
     * Test for a valid user
     */
    @Test
    public void getUserByIdUserExists() {
        Email email = new Email("abc@cool.com");
        Name name = new Name("test");
        UserAffiliation userAffiliation = new UserAffiliation("test");
        Link link = new Link("test");
        Communication com = new Communication("communicateMe");
        AppUser user = new AppUser(email, name, userAffiliation, link, com);
        userRepository.save(user);
        List<AppUser> ids = userRepository.findAll();
        assertEquals(ids.get(0), userController.getUserById(0).getBody());
    }

}
