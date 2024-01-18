package nl.tudelft.sem.template.services;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.logs.LogFactory;
import nl.tudelft.sem.template.logs.attendee.AttendeeLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service Class handling attendance.
 */
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
@Service
public class AttendeeService {

    private final transient AttendeeRepository attendeeRepository;
    private final transient UserRepository userRepository;
    private final transient EventRepository eventRepository;
    private final transient TrackRepository trackRepository;
    private final transient AttendeeLogFactory attendeeLogFactory;

    /**
     * A constructor dependency injection for the various JPA Repositories.
     *
     * @param attendeeRepository    the attendee repository injection
     * @param userRepository        the user repository injection
     * @param eventRepository       the event repository injection
     * @param trackRepository       the track repository injection
     */
    @Autowired
    public AttendeeService(AttendeeRepository attendeeRepository, UserRepository userRepository,
                           EventRepository eventRepository, TrackRepository trackRepository) {
        this.attendeeRepository = attendeeRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.trackRepository = trackRepository;
        attendeeLogFactory = (AttendeeLogFactory) LogFactory.loadFactory(LogType.ATTENDEE);
    }

    /**
     * Creates a new attendance instance, which is committed to the
     * attendee repository. In case where such an attendance instance already
     * exists,
     * confirmed or not, the method throws an {@link IllegalArgumentException} to
     * signify
     * that no change has taken place.
     *
     * @param userId    the user identifier of the attendance to be created
     * @param eventId   the event identifier of the attendance to be created
     * @param trackId   the track identifier of the attendance to be created
     * @param role      the role of the new attendance
     * @param confirmed the confirmation status of the attendance.
     * @return the newly created instance.
     * @throws IllegalArgumentException specifies that the attendance already
     *                                  exists, and that no changes have been made
     *                                  to the repository.
     */
    @Transactional
    public Attendee createAttendance(Long userId, Long eventId, Long trackId, RoleTitle role, boolean confirmed)
            throws IllegalArgumentException {

        // Check that no such attendance already exists
        if (attendeeRepository.existsByUserIdAndEventIdAndTrackId(userId, eventId, trackId)) {
            throw new IllegalArgumentException("Attendance instance already exists.");
        }

        // User and event are contractually bound to exist already.
        AppUser user = userRepository.findById(userId).get();
        Event event = eventRepository.findById(eventId).get();
        Track track = trackRepository.findById(trackId).orElse(null);

        Attendee attendee = new Attendee(
                new Role(role), new Confirmation(confirmed), event, track, user);


        // Commits the new attendance to the repository
        attendee = attendeeRepository.save(attendee);
        attendeeLogFactory.registerCreation(attendee);
        return  attendee;

    }

    /**
     * Deletes the attendance corresponding to the provided identifiers
     * if it already exists in the database. Otherwise, if it does not
     * exist, then no deletion takes place, the database remains unmodified,
     * and a {@link NoSuchElementException} is thrown.
     *
     * @param id                        the attendance identifier
     * @throws NoSuchElementException   indicates that no such attendance
     *                                  can be found, and that no deletion can take
     *                                  place.
     */
    @Transactional
    public void deleteAttendance(Long id)
            throws NoSuchElementException {
        // Retrieve the attendance instance from the database.
        Optional<Attendee> retrievedAttendance = attendeeRepository.findById(id);

        // Exception handling for when no attendances can be found.
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found, so no deletion is possible.");
        }

        Attendee attendee = retrievedAttendance.get();

        // Deletes the Attendee instance associated with the given key
        attendeeRepository.delete(attendee);
        attendeeLogFactory.registerRemoval(attendee);
    }

    /**
     * Retrieves the attendance object corresponding to the provided
     * identifier. If the object does not exist, then ```null```
     * is returned.
     *
     * @param id the Attendee identifier
     */
    public Attendee getAttendance(Long id) {
        return attendeeRepository.findById(id).orElse(null);
    }

    /**
     * Confirms whether an attendance object corresponding to the
     * given identifier exists in the database. Returns ```True```
     * if it exists, ```False``` otherwise.
     *
     * @param id the Attendee identifier
     * @return ```True``` if the attendance object exists.
     */
    public boolean exists(Long id) {

        return attendeeRepository.existsById(id);
    }

    /**
     * Retrieves all attendances corresponding to the given filters. A filter
     * is not considered if it is null. A completely null
     * argument vector will return the list of all attendances present in the
     * database. If no such attendances exist, then a {@link NoSuchElementException}
     * is thrown.
     *
     * @param userId                    the user filter
     * @param eventId                   the event filter
     * @param trackId                   the track filter
     * @param confirmed                 the confirmation status filter
     * @return                          the list of corresponding attendances
     * @throws NoSuchElementException   indicates that no such attendances
     *                                  exist.
     */
    public List<Attendee> getFilteredAttendance(Long userId,
                                                Long eventId,
                                                Long trackId,
                                                Boolean confirmed)
            throws NoSuchElementException {

        // Retrieve the object instances
        AppUser user = null;
        Event event = null;
        Track track = null;

        if (userId != null) {
            var retrieved = userRepository.findById(userId);
            // If no user can be found, then there are no associated attendances.
            if (retrieved.isEmpty()) {
                throw new NoSuchElementException("No confirmed attendance associated with this user can be found.");
            }
            user = retrieved.get();
        }

        if (eventId != null) {
            var retrieved = eventRepository.findById(eventId);
            // If no event can be found, then there are no associated attendances.
            if (retrieved.isEmpty()) {
                throw new NoSuchElementException("No confirmed attendance associated with this user can be found.");
            }
            event = retrieved.get();
        }

        if (trackId != null) {
            var retrieved = trackRepository.findById(trackId);
            // If no track can be found, then there are no associated attendances.
            if (retrieved.isEmpty()) {
                throw new NoSuchElementException("No confirmed attendance associated with this user can be found.");
            }
            track = retrieved.get();
        }

        // Retrieve the filtered list
        Confirmation confirmation = (confirmed == null) ? null : new Confirmation(confirmed);
        List<Attendee> retrievedList = attendeeRepository.findFiltered(user, event, track, confirmation);

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this user can be found.");
        }

        // Return the filtered list
        return retrievedList;
    }

    /**
     * Modifies the role of the corresponding attendance if it exists. Otherwise,
     * a {@link NoSuchElementException} is thrown to indicate that the attendance
     * does not exist.
     *
     * @param id         the attendance identifier
     * @param role       the new role
     * @throws NoSuchElementException indicates that no such attendance
     *                                exists, and, therefore, that no modification
     *                                can take place.
     */
    @Transactional
    public Attendee modifyTitle(Long id, RoleTitle role)
            throws NoSuchElementException {
        if (id == null) {
            throw new NoSuchElementException("No such attendance is found; cannot be modified.");
        }
        Optional<Attendee> retrievedAttendance = attendeeRepository.findById(id);

        // Exception handling for when the repository can find the instance
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No such attendance is found; cannot be modified.");
        }

        Attendee attendee = retrievedAttendance.get();

        attendee.setRole(new Role(role));

        // Commit the changes
        attendeeLogFactory.registerConfirmationChange(attendee);
        return attendeeRepository.save(attendee);

    }

    /**
     * Checks whether the executor has sufficient permission to modify
     * the subject according to attendance roles.
     *
     * @param executorId                    the identifier of the executor user.
     * @param subjectId                     the identifier of the subject attendance.
     * @return                              ```True``` if the executor has sufficient permission.
     * @throws IllegalArgumentException     if any of the identifiers do not correspond to any persistent object.
     * @throws NoSuchElementException       the subject is not attending.=
     */
    @Transactional
    public boolean suffices(Long executorId,
                            Long subjectId)
            throws IllegalArgumentException, NoSuchElementException {

        // retrieve all necessary objects
        Attendee subject = attendeeRepository
                .findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject."));

        if (subject.getUser().getId().equals(executorId)) {
            return true;
        }

        List<Attendee> executorAttendees;
        try {
            executorAttendees = getFilteredAttendance(executorId, subject.getEvent().getId(),
                    null, true);

            // Remove irrelevant attendances.
            executorAttendees.removeIf((x) -> (subject.getTrack() == null && x.getTrack() != null)
                    || (x.getTrack() != null
                    && !Objects.equals(x.getTrack().getId(), subject.getTrack().getId())));

            if (executorAttendees.isEmpty()) {
                return false;
            }

        } catch (NoSuchElementException e) {
            return false;
        }

        // Extract the greatest permission
        executorAttendees.sort(Comparator.comparingLong(x -> x.getRole().getRoleTitle().getPermission()));

        return executorAttendees.get(0)
                .getRole()
                .getRoleTitle()
                .getPermission()
                <= subject
                .getRole()
                .getRoleTitle()
                .getPrecedence();

    }
}
