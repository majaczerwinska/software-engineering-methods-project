package nl.tudelft.sem.template.controllers;

import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.model.User;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for methods related to the User entity.
 */
@RestController
public class UserController implements UserApi {
    private final transient UserService userService;

    /**
     * Instantiates a new User controller.
     *
     * @param userService used to manage user services
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Checks whether a user with the given id exists, retrieves it if yes.

     * @param userId - id of the to be found user
     * @return - bad request if invalid id, unauthorized access if expired token,
     *           not found if user not found, appUser if user found
     */
    @Override
    @Transactional
    public ResponseEntity<User> getAccountByID(@PathVariable("userID") Long userId) {
        if (userId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //TODO: unauthorized access
        if (!userService.userExistsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.getUserById(userId).toModelUser());
    }
    /**
     * Checks whether a user with the given email address exists, retrieves it if yes.

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
        //TODO: unauthorized access
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
    @PreAuthorize("@RoleService.hasPermission(userService, authManager, attendeeService, "
            + "#track.getEventId(), #track.getId(), 0)") // 401
    public ResponseEntity<User> createAccount(@Valid @RequestBody User user) {
        // Check if the appUser is null or has missing required fields
        try {
            userService.createUser(new AppUser(user));
            return ResponseEntity.ok(user); // 200
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Invalid user data")) {
                return ResponseEntity.badRequest().build(); // 400
            } else {
                return ResponseEntity.status(409).build(); // 409, user already exists
            }
        }

    }

    /**
     * This method updates an existing User Account.
     *
     * @param updatedUser - user account to be updated
     *
     * @return responseEntity of method
     */
    @Override
    @Transactional
    @PreAuthorize("@RoleService.hasPermission(userService, authManager, attendeeService, "
            + "#track.getEventId(), #track.getId(), 0)") // 401
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody User updatedUser) {
        // Check if the updatedUser is null or has missing required fields
        try {
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
    @PreAuthorize("@RoleService.hasPermission(userService, authManager, attendeeService, "
            + "#track.getEventId(), #track.getId(), 0)") // 401
    public ResponseEntity<Void> deleteAccount(@PathVariable("userID") Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }

    }

}


