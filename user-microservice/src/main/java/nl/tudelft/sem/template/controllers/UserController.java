package nl.tudelft.sem.template.controllers;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
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
public class UserController {
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
    @GetMapping("/user/{userID}")
    public ResponseEntity<AppUser> getUserById(@PathVariable("userID") long userId) {
        if (userId < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //TODO: unauthorized access
        if (!userService.userExistsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    /**
     * Checks whether a user with the given email address exists, retrieves it if yes.

     * @param email - email of the to be found user
     * @return - bad request if invalid email, unauthorized access if expired token,
     *           not found if user not found, appUser if user found
     */
    @GetMapping("user/byEmail/{email}")
    public ResponseEntity<AppUser> getUserByEmail(@PathVariable("email") Email email) {
        if (!email.toString().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //TODO: unauthorized access
        if (!userService.userExistsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    /**
     * Create a new user account.
     *
     * @param appUser - the RequestBody to create a new User account with
     * @return ResponseEntity of new User account
     */
    @PostMapping("/user")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        // Check if the appUser is null or has missing required fields
        if (appUser == null || appUser.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the user already exists
        if (userService.userExistsByEmail(appUser.getEmail())) {
            return ResponseEntity.status(409).build(); // HTTP 409 User already exists
        }

        // TODO: check if the request is authorized, i don't know how to do this

        AppUser createdUser = userService.createUser(appUser);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * This method updates an existing User Account.
     *
     * @param updatedUser - user account to be updated
     *
     * @return responseEntity of method
     */
    @PutMapping("/user")
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser updatedUser) {
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
        existingUser.setName(updatedUser.getName());
        existingUser.setAffiliation(updatedUser.getAffiliation());
        existingUser.setCommunication(updatedUser.getCommunication());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setLink(updatedUser.getLink());

        // Save the updated user to the database
        AppUser updatedUserResult = userService.updateUser(existingUser);

        return ResponseEntity.ok(updatedUserResult);

    }

    /**
     * This method deletes an existing user account by ID.
     *
     * @param userId - UserID of the user account that needs to be deleted
     * @return response entity of the executed method
     */
    @DeleteMapping("/user/{userID}")
    public ResponseEntity<AppUser> deleteUser(@PathVariable("userID") int userId) {
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


