package nl.tudelft.sem.template.controllers;

import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.api.EventApi;
import nl.tudelft.sem.template.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class EventController implements EventApi {

    private final transient AuthManager authManager;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public EventController(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public ResponseEntity<Event> addEvent(
        Event event
        ) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
