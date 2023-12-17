package nl.tudelft.sem.template.example.controllers;

import nl.tudelft.sem.template.domain.user.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * This controller is responsible for methods related to the User entity.
 *
 */
@RestController
public class UserController {

//    /**
//     * Create a new user account.
//     *
//     * @param appUser - the RequestBody to create a new User account with
//     * @return ResponseEntity of new User account
//     */
//    @PostMapping("/user")
//    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
//        // Check if the appUser is null or has missing required fields
//        if (appUser == null || appUser.getEmail() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        // Check if the user already exists (example)
//        if (userService.userExistsByEmail(appUser.getEmail())) {
//            return ResponseEntity.status(409).build(); // HTTP 409 User already exists
//        }
//
//        // TODO: check if the request is authorized, i don't know how to do this
//        // if ("request is not authorized") {
//        // return ResponseEntity.status(401).build(); // HTTP 401 Unauthorized
//        // }
//
//        AppUser createdUser = userService.createUser(appUser);
//
//        // Return a response with the created user data
//        return ResponseEntity.ok(createdUser);
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

}
