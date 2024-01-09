package nl.tudelft.sem.template.services;

import java.util.List;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.NonNull;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.enums.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
     * Creates a new unconfirmed attendance instance, which is committed to the
     * event repository. In case where such an attendance instance already exists,
     * confirmed or not, the method throws an {@link IllegalArgumentException} to signify
     * that no change has taken place.
     *
     * <p>This method is to be used within the service
     * class; refer to the invitation-related methods for out-of-class invocations.
     *
     * @param userId the user identifier of the attendance to be created
     * @param eventId the event identifier of the attendance to be created
     * @param trackId the track identifier of the attendance to be created
     * @param role the role of the new attendance
     * @throws IllegalArgumentException specifies that the attendance already
     *          exists, and that no changes have been made to the repository.
     */
    @Transactional
    public Event createEvent(LocalDate startDate, LocalDate endDate, boolean isCancelled, String name, String description)
            throws IllegalArgumentException {

        Event event = new Event(startDate, endDate, new IsCancelled(isCancelled), new EventName(name), new EventDescription(description));

        // Commits the new attendance to the repository
        return repository.save(event);

    }
}
