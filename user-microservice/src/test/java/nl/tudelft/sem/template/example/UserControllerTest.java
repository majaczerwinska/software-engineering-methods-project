package nl.tudelft.sem.template.example;

import io.jsonwebtoken.Claims;
import nl.tudelft.sem.template.domain.user.*;
import nl.tudelft.sem.template.example.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    private UserService userService;
    private UserRepositoryTest userRepository;
    private UserController userController;
    final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");

    @BeforeEach
    public void setup() {
        userRepository = new UserRepositoryTest();
        userService = new UserService(userRepository);
        userController = new UserController(userService);
    }

    @Test
    public void getUserByIdInvalidId(){
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(), userController.getUserById(-1));
    }

    @Test
    public void getUserByIdUnauthorized(){
        //TODO: !!!
    }

    @Test
    public void getUserByIdUserNonexistent(){
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), userController.getUserById(0));
    }

    @Test
    public void getUserByIdUserExists(){
        Email email = new Email("abc@cool.com");
        AppUser user = new AppUser(email, testHashedPassword, "testName", "affiliation", "link", "comm");
        userRepository.save(user);
        List<AppUser> ids = userRepository.findAll();
        assertEquals(ids.get(0), userController.getUserById(0).getBody());
    }

}
