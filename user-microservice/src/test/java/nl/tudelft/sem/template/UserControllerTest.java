package nl.tudelft.sem.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.template.controllers.UserController;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.help.UserRepositoryTest;
import nl.tudelft.sem.template.model.User;
import nl.tudelft.sem.template.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private  UserRepositoryTest userRepository;

    @InjectMocks
    private UserController userController;

    private AppUser appUser;

    /**
     * Setting up the environment.
     */
    @BeforeEach
    public void setup() {
        userRepository = new UserRepositoryTest();
        userService = new UserService(userRepository);
        userController = new UserController(userService);
        Long id = 1L;
        Email email = new Email("abc@fun.org");
        Name name = new Name("user");
        UserAffiliation affiliation = new UserAffiliation("affiliation");
        Link link = new Link("link");
        Communication communication = new Communication("communication");
        appUser = new AppUser(id, email, name, name, affiliation, link, communication);
    }

    /**
     * Test for getting an invalid id.
     */
    @Test
    public void getUserByIdInvalidId() {
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(), userController.getAccountByID(-1L));
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
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), userController.getAccountByID(0L));
    }

    /**
     * Test for a valid user.
     */
    @Test
    public void getUserByIdUserExists() {
        Email email = new Email("abc@cool.com");
        Name name = new Name("test");
        UserAffiliation userAffiliation = new UserAffiliation("test");
        Link link = new Link("test");
        Communication com = new Communication("communicateMe");
        AppUser user = new AppUser(1L, email, name, name, userAffiliation, link, com);
        userRepository.save(user);
        AppUser appUser = userRepository.findById(String.valueOf(1L)).get();
        assertEquals(appUser.toModelUser(), userController.getAccountByID(1L).getBody());
    }

    @Test
    public void getAccountByEmailUserExists() {
        userRepository.save(appUser);
        AppUser appUser1 = userRepository.findByEmail(appUser.getEmail()).get();
        assertEquals(appUser1.toModelUser(),
                userController.getAccountByEmail(appUser.getEmail().toString()).getBody());
    }

    @Test
    public void getAccountByEmailInvalidEmail() {
        appUser.setEmail(new Email("notAnEmail"));
        userRepository.save(appUser);
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                userController.getAccountByEmail(appUser.getEmail().toString()));
    }

    @Test
    public void getAccountByEmailNonExistent() {
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
                userController.getAccountByEmail(appUser.getEmail().toString()));
    }

    @Test
    public void createAccountNullUser() {
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                userController.createAccount(null));
    }

    @Test
    public void createAccountValidUser() {
        User modelUser = appUser.toModelUser();
        assertEquals(modelUser,
                userController.createAccount(modelUser).getBody());
    }

    @Test
    public void createAccountUserAlreadyExists() {
        userRepository.save(appUser);
        assertEquals(ResponseEntity.status(409).build(),
                userController.createAccount(appUser.toModelUser()));
    }

    @Test
    public void updateAccountValidUser() {
        userRepository.save(appUser);
        User modelUser = appUser.toModelUser();
        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(),
                userController.updateAccount(modelUser));
    }

    @Test
    public void updateAccountInvalidUser() {
        appUser.setId(-1L);
        userRepository.save(appUser);
        User modelUser = appUser.toModelUser();
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                userController.updateAccount(modelUser));
    }

    @Test
    public void updateAcountUserNonExistent() {
        User modelUser = appUser.toModelUser();
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
                userController.updateAccount(modelUser));
    }

    @Test
    public void deleteAccountValid() {
        userRepository.save(appUser);
        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(),
                userController.deleteAccount(appUser.getId()));
    }

    @Test
    public void deleteAccountInvalidUser() {
        appUser.setId(-1L);
        userRepository.save(appUser);
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                userController.deleteAccount(appUser.getId()));
    }

    @Test
    public void deleteAccountUserNonExistent() {
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
                userController.deleteAccount(appUser.getId()));
    }


}
