package nl.tudelft.sem.template.controllers;

import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.api.EventApi;
import nl.tudelft.sem.template.model.Event;
import nl.tudelft.sem.template.services.EventService;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final transient EventService eventService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public EventController(AuthManager authManager, EventService eventService) {
        this.authManager = authManager;
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<Event> addEvent(
        Event event
        ) {
        nl.tudelft.sem.template.domain.event.Event createdEvent = eventService.createEvent(event.getStartDate(), event.getEndDate(), event.getIsCancelled(), event.getName(), event.getDescription());
        return ResponseEntity.ok(eventResponse(createdEvent));
    }

    private static Event eventResponse(nl.tudelft.sem.template.domain.event.Event event) {
        Event returnedEvent = new Event();
        returnedEvent.setId(event.getId());
        returnedEvent.setStartDate(event.getStartDate());
        returnedEvent.setEndDate(event.getEndDate());
        returnedEvent.setIsCancelled(event.getIsCancelled().getCancelStatus());
        returnedEvent.setName(event.getName().toString());
        returnedEvent.setDescription(event.getDescription().toString());
        return returnedEvent;
    }
}
