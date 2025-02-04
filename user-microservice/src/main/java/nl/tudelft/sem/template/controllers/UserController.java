package nl.tudelft.sem.template.controllers;

import java.util.NoSuchElementException;
import java.util.Objects;
import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.model.User;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for methods related to the User entity.
 */
@RestController
public class UserController implements UserApi {
    private final transient UserService userService;
    private final transient AuthManager authManager;

    /**
     * Instantiates a new User controller.
     *
     * @param userService used to manage user services
     * @param authManager Used for authentication-related checks.
     */
    @Autowired
    public UserController(UserService userService, AuthManager authManager) {
        this.userService = userService;
        this.authManager = authManager;
    }

    /**
     * Checks whether a user with the given id exists, retrieves it if yes.
     *
     * @param userId - id of the to be found user
     * @return - bad request if invalid id, unauthorized access if expired token,
     *           not found if user not found, appUser if user found
     */
    @Override
    @Transactional
    public ResponseEntity<User> getAccountByID(@PathVariable("userID") Long userId) {
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userService.userExistsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.getUserById(userId).toModelUser());
    }

    /**
     * Checks whether a user with the given email address exists, retrieves it if yes.
     *
     * @param email - email of the to be found user
     * @return - bad request if invalid email, unauthorized access if expired token,
     *           not found if user not found, appUser if user found
     */
    @Override
    @Transactional
    public ResponseEntity<User> getAccountByEmail(@PathVariable("email") String email) {
        if (!email.contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userService.userExistsByEmail(new Email(email))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.getUserByEmail(new Email(email)).toModelUser());
    }

    /**
     * Create a new user account.
     *
     * @param user - the RequestBody to create a new User account with
     * @return ResponseEntity of new User account
     */
    @Override
    @Transactional
    public ResponseEntity<User> createAccount(@RequestBody User user) {
        try {
            userService.createUser(new AppUser(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        } catch (EntityExistsException e) {
            return ResponseEntity.status(409).build(); // 409, user already exists
        }
        return ResponseEntity.ok(user); //200

    }

    /**
     * This method updates an existing User Account.
     *
     * @param updatedUser - user account to be updated
     * @return responseEntity of method
     */
    @Override
    @Transactional
    public ResponseEntity<Void> updateAccount(@RequestBody User updatedUser) {
        // Check if the updatedUser is null or has missing required fields
        try {
            if (!userService.userExistsByEmail(new Email(authManager.getEmail()))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            userService.updateUser(new AppUser(updatedUser));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * This method deletes an existing user account by ID.
     *
     * @param userId - UserID of the user account that needs to be deleted
     * @return response entity of the executed method
     */
    @Override
    @Transactional
    public ResponseEntity<Void> deleteAccount(@PathVariable("userID") Long userId) {
        try {
            Email email = new Email(authManager.getEmail());
            if (!userService.userExistsByEmail(email)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            AppUser user = userService.getUserByEmail(email);
            if (!Objects.equals(user.getId(), userId)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }

    }

}
