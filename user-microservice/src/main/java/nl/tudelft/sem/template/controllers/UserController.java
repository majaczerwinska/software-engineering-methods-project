package nl.tudelft.sem.template.controllers;

import javax.transaction.Transactional;
import nl.tudelft.sem.template.api.UserApi;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.model.User;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        if (!email.toString().contains("@")) {
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
    public ResponseEntity<User> createAccount(@RequestBody User user) {
        // Check if the appUser is null or has missing required fields
        if (user == null || user.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the user already exists
        if (userService.userExistsByEmail(new Email(user.getEmail()))) {
            return ResponseEntity.status(409).build(); // HTTP 409 User already exists
        }

        // TODO: check if the request is authorized, i don't know how to do this
        AppUser appUserToSave = new AppUser(new Email(user.getEmail()), new Name(user.getFirstName()),
                new Name(user.getLastName()), new UserAffiliation(user.getAffiliation()),
                new Link(user.getPersonalWebsite()), new Communication(user.getPreferredCommunication()));
        AppUser createdUser = userService.createUser(appUserToSave);
        return ResponseEntity.ok(createdUser.toModelUser());
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
    public ResponseEntity<Void> updateAccount(@RequestBody User updatedUser) {
        // Check if the updatedUser is null or has missing required fields
        if (updatedUser == null || updatedUser.getId() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the user exists
        AppUser existingUser = userService.getUserById(updatedUser.getId());
        if (existingUser == null) {
            return ResponseEntity.status(404).build(); // HTTP 404 User not found
        }

        // TODO: Check if the request is authorized.

        // Update the user properties
        existingUser.setFirstName(new Name(updatedUser.getFirstName()));
        existingUser.setLastName(new Name(updatedUser.getLastName()));
        existingUser.setAffiliation(new UserAffiliation(updatedUser.getAffiliation()));
        existingUser.setCommunication(new Communication(updatedUser.getPreferredCommunication()));
        existingUser.setEmail(new Email(updatedUser.getEmail()));
        existingUser.setLink(new Link(updatedUser.getPersonalWebsite()));

        // Save the updated user to the database
        userService.updateUser(existingUser);

        return ResponseEntity.status(HttpStatus.OK).body(null);

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
        if (userId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the user exists
        AppUser existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(404).build(); // HTTP 404 User not found
        }

        // TODO: Check if the request is authorized.

        // Delete the user from the repository
        userService.deleteUserById(userId);

        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

}


