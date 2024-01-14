package nl.tudelft.sem.template.services;

import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service Class handling invitations and attendance.
 */
@Service
public class EventService {

    private final transient EventRepository eventRepository;
    private final transient UserRepository userRepository;
    private final transient AttendeeRepository attendeeRepository;


    /**
     * A constructor dependency injection for the Event JPA Repository concrete
     * implementation.
     *
     * @param repository the event repository injection
     */
    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, AttendeeRepository attendeeRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.attendeeRepository = attendeeRepository;
    }

    /**
     * Creates a new event,
     */
    @Transactional
    public Event createEvent(LocalDate startDate, LocalDate endDate, boolean isCancelled, String name,
        String description, String email) {

        Event event = new Event(startDate, endDate, new IsCancelled(isCancelled), new EventName(name),
            new EventDescription(description));
        Event returnedEvent = eventRepository.save(event);

        AppUser user = userRepository.findByEmail(new Email(email)).get();

        Attendee attendee = new Attendee(new Role(RoleTitle.GENERAL_CHAIR), new Confirmation(true), returnedEvent, null, user);
        attendeeRepository.save(attendee);

        return returnedEvent;
    }

    /**
     * Updates a new event,
     */
    @Transactional
    public Event updateEvent(Long id, LocalDate startDate, LocalDate endDate, boolean isCancelled, String name, String description) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        Event newEvent = new Event(id, startDate, endDate, new IsCancelled(isCancelled), new EventName(name), new EventDescription(description));
        Event returnedEvent = eventRepository.save(newEvent);
        return returnedEvent;
    }
}
