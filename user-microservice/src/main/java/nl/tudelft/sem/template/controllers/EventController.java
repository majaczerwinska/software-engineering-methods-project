package nl.tudelft.sem.template.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import nl.tudelft.sem.template.api.EventApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.Event;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.EventService;
import nl.tudelft.sem.template.services.RoleService;
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
    private final transient UserRepository userRepository;
    private final transient EventRepository eventRepository;
    private final transient RoleService roleService;
    private final transient UserService userService;
    private final transient AttendeeService attendeeService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and
     *                    authorize the user
     */
    @Autowired
    public EventController(AuthManager authManager, EventService eventService,
        UserRepository userRepository, EventRepository eventRepository, RoleService roleService, UserService userService, AttendeeService attendeeService) {
        this.authManager = authManager;
        this.eventService = eventService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.roleService = roleService;
        this.userService = userService;
        this.attendeeService = attendeeService;
    }

    @Override
    @Transactional
    public ResponseEntity<Event> addEvent(
            Event event) {
        if (!userRepository.existsByEmail(new Email(authManager.getEmail()))) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        nl.tudelft.sem.template.domain.event.Event createdEvent;
        try {
            createdEvent = eventService.createEvent(event.getStartDate(),
                    event.getEndDate(), event.getIsCancelled(), event.getName(), event.getDescription(),
                    authManager.getEmail());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(createdEvent.toModelEvent());
    }

    @Override
    @Transactional
    public ResponseEntity<List<Event>> findEvent(
            LocalDate startBefore,
            LocalDate startAfter,
            LocalDate endBefore,
            LocalDate endAfter,
            Boolean cancelled,
            String name) {
        List<nl.tudelft.sem.template.domain.event.Event> events;
        try {
            events = eventRepository.findByOptionalParams(startBefore, startAfter, endBefore, endAfter,
                    new IsCancelled(cancelled), new EventName(name));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Event> returnEvents = events.stream().map(nl.tudelft.sem.template.domain.event.Event::toModelEvent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(returnEvents);
    }

    @Override
    @Transactional
    public ResponseEntity<Event> updateEvent(Event event) {
        if (!roleService.hasPermission(userService, authManager, attendeeService, event.getId(), null, 0)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        nl.tudelft.sem.template.domain.event.Event returnedEvent;
        try {
            returnedEvent = eventService.updateEvent(event.getId(), event.getStartDate(),
                    event.getEndDate(), event.getIsCancelled(), event.getName(), event.getDescription());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(returnedEvent.toModelEvent());
    }
}
