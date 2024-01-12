package nl.tudelft.sem.template.services;

import java.time.LocalDate;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service Class handling invitations and attendance.
 */
@Service
public class EventService {

    private final transient EventRepository repository;

    /**
     * A constructor dependency injection for the Event JPA Repository concrete
     * implementation.
     *
     * @param repository the event repository injection
     */
    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    // Private Methods

    /**
     * Creates a new event,
     *
     * <p>This method is to be used within the service
     * class; refer to the invitation-related methods for out-of-class invocations.
     *
     */
    @Transactional
    public Event createEvent(LocalDate startDate, LocalDate endDate, boolean isCancelled, String name,
            String description) {

        Event event = new Event(startDate, endDate, new IsCancelled(isCancelled), new EventName(name),
                new EventDescription(description));

        return repository.save(event);
    }
}
