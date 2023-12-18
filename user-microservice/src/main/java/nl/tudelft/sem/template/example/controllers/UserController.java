package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for methods related to the User entity.
 */
@RestController
public class UserController {
    private final transient UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    @PostMapping("/user")
    //    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser){
    //
    //    }

    //    @PutMapping("/user")
    //    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser appUser){
    //
    //    }
    //
    //    @DeleteMapping("/user/{userID}")
    //    public ResponseEntity<AppUser> deleteUser(@PathVariable("userID") int userID){
    //
    //    }

    /**
     * Checks whether a user with the given id exists, retrieves it if yes.

     * @param userId - id of the to be found user
     * @return - bad request if invalid id, unauthorized access if expired token,
     *           not found if user not found, appUser if user found
     */
    @GetMapping("/user/{userID}")
    public ResponseEntity<AppUser> getUserById(@PathVariable("userID") int userId) {
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


}


