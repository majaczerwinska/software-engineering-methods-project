package nl.tudelft.sem.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.controllers.UserController;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.model.User;
import nl.tudelft.sem.template.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles("test")
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthManager authManager;

    @InjectMocks
    private UserController userController;

    private AppUser appUser;

    /**
     * Setting up the environment.
     */
    @BeforeEach
    public void setup() {
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
        when(userService.userExistsById(eq(1L))).thenReturn(true);
        when(userService.getUserById(eq(1L))).thenReturn(appUser);
        assertEquals(appUser.toModelUser(), userController.getAccountByID(1L).getBody());
    }

    @Test
    public void getAccountByEmailUserExists() {
        when(userService.userExistsByEmail(eq(appUser.getEmail()))).thenReturn(true);
        when(userService.getUserByEmail(eq(appUser.getEmail()))).thenReturn(appUser);
        assertEquals(appUser.toModelUser(),
                userController.getAccountByEmail(appUser.getEmail().toString()).getBody());
        verify(userService, times(1)).userExistsByEmail(eq(appUser.getEmail()));
        verify(userService, times(1)).getUserByEmail(eq(appUser.getEmail()));
    }

    @Test
    public void getAccountByEmailInvalidEmail() {
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                userController.getAccountByEmail("notAnEmail"));
    }

    @Test
    public void getAccountByEmailNonExistent() {
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
                userController.getAccountByEmail(appUser.getEmail().toString()));
    }

    @Test
    public void createAccountNullUser() {
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(true);

        ResponseEntity<User> response = userController.createAccount(null);

        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(eq(email)); // Corrected verification
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createAccountNullEmail() {
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(true);

        appUser.setEmail(new Email("noEmail"));
        User modelUser = appUser.toModelUser();

        ResponseEntity<User> response = userController.createAccount(modelUser);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void createAccountNonAuthUser() {
        User modelUser = appUser.toModelUser();
        when(authManager.getEmail()).thenReturn(appUser.getEmail().toString());
        when(userService.userExistsByEmail(eq(appUser.getEmail()))).thenReturn(false);

        ResponseEntity<User> response = userController.createAccount(modelUser);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(eq(appUser.getEmail()));
    }

    @Test
    public void createAccountValidUser() {
        User modelUser = appUser.toModelUser();
        when(authManager.getEmail()).thenReturn(appUser.getEmail().toString());
        when(userService.userExistsByEmail(eq(appUser.getEmail()))).thenReturn(true);
        when(userService.createUser(eq(appUser))).thenReturn(appUser);
        ResponseEntity<User> response = userController.createAccount(modelUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(eq(appUser.getEmail()));
        verify(userService, times(1)).createUser(eq(appUser));
    }

    @Test
    public void createAccountUserAlreadyExists() {
        when(authManager.getEmail()).thenReturn(appUser.getEmail().toString());
        when(userService.userExistsByEmail(eq(appUser.getEmail()))).thenReturn(true);
        when(userService.createUser(eq(appUser))).thenThrow(new IllegalArgumentException("User already exists"));
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
    public void updateAccountUserNonExistent() {
        when(userService.updateUser(eq(appUser))).thenThrow(new NoSuchElementException());
        User modelUser = appUser.toModelUser();
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
                userController.updateAccount(modelUser));
    }

    @Test
    public void deleteAccountValid() {
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(true);
        when(userService.getUserByEmail(eq(email))).thenReturn(appUser);
        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(),
                userController.deleteAccount(appUser.getId()));
    }

    @Test
    public void deleteAccountInvalidUser() {
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(true);
        when(userService.getUserByEmail(eq(email))).thenReturn(appUser);
        doThrow(new IllegalArgumentException()).when(userService).deleteUser(appUser.getId());
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),
                userController.deleteAccount(appUser.getId()));
    }

    @Test
    public void deleteAccountUserNonExistent() {
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(true);
        when(userService.getUserByEmail(eq(email))).thenReturn(appUser);
        doThrow(new NoSuchElementException()).when(userService).deleteUser(appUser.getId());
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
                userController.deleteAccount(appUser.getId()));
    }

    @Test
    public void deleteAccountUserUnauthorized() {
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(false);
        ResponseEntity<Void> response = userController.deleteAccount(appUser.getId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(eq(appUser.getEmail()));

    }

    @Test
    public void deleteAccountUserUnauthorized2() {
        AppUser user2 = new AppUser(2L);
        Email email = appUser.getEmail();
        when(authManager.getEmail()).thenReturn(email.toString());
        when(userService.userExistsByEmail(eq(email))).thenReturn(true);
        when(userService.getUserByEmail(email)).thenReturn(user2);
        ResponseEntity<Void> response = userController.deleteAccount(appUser.getId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(eq(appUser.getEmail()));

    }


}
