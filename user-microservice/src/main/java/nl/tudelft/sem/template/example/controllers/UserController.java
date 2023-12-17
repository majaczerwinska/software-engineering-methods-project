package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import nl.tudelft.sem.template.domain.user.UserService;


/**
 * This controller is responsible for methods related to the User entity.
 *
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

//    @PutMapping("/user")
//    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser appUser){
//
//    }
//
//    @DeleteMapping("/user/{userID}")
//    public ResponseEntity<AppUser> deleteUser(@PathVariable("userID") int userID){
//
//    }
//
//    @GetMapping("/user/{userID}")
//    public ResponseEntity<AppUser> getUserById(@PathVariable("userID") int userID){
//    }
//
//    @GetMapping("user/byEmail/{email}")
//    public ResponseEntity<AppUser> getUserByEmail(@PathVariable("email") String email){
//
//    }

}
