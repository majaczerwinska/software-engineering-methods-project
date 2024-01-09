package nl.tudelft.sem.template.controllers;

import nl.tudelft.sem.template.api.EventApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.Event;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.EventService;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Event API controller.
 */
@RestController
public class EventController implements EventApi {

    private final transient AuthManager authManager;
    private final transient EventService eventService;
    private final transient AttendeeService attendeeService;
    private final transient UserService userService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and
     *                    authorize the user
     */
    @Autowired
    public EventController(AuthManager authManager, EventService eventService, AttendeeService attendeeService,
            UserService userService) {
        this.authManager = authManager;
        this.eventService = eventService;
        this.attendeeService = attendeeService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Event> addEvent(
            Event event) {
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        nl.tudelft.sem.template.domain.event.Event createdEvent = eventService.createEvent(event.getStartDate(),
                event.getEndDate(), event.getIsCancelled(), event.getName(), event.getDescription());
        attendeeService.createAttendance(user.getId(), event.getId(), null, RoleTitle.GENERAL_CHAIR, true);
        return ResponseEntity.ok(createdEvent.toModelEvent());
    }
}
