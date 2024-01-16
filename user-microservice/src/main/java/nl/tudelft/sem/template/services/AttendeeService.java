package nl.tudelft.sem.template.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.NonNull;
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
import nl.tudelft.sem.template.enums.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * A service Class handling invitations and attendance.
 */
@Service
public class AttendeeService {

    private final transient AttendeeRepository attendeeRepository;
    private final transient UserRepository userRepository;
    private final transient EventRepository eventRepository;
    private final transient TrackRepository trackRepository;

    /**
     * A constructor dependency injection for the Attendee JPA Repository concrete
     * implementation.
     *
     * @param attendeeRepository the attendee repository injection
     */
    @Autowired
    public AttendeeService(AttendeeRepository attendeeRepository, UserRepository userRepository,
            EventRepository eventRepository, TrackRepository trackRepository) {
        this.attendeeRepository = attendeeRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.trackRepository = trackRepository;
    }

    // Private Methods

    /**
     * Creates a new unconfirmed attendance instance, which is committed to the
     * attendee repository. In case where such an attendance instance already
     * exists,
     * confirmed or not, the method throws an {@link IllegalArgumentException} to
     * signify
     * that no change has taken place.
     *
     * <p>This method is to be used within the service
     * class; refer to the invitation-related methods for out-of-class invocations.
     *
     * @param userId  the user identifier of the attendance to be created
     * @param eventId the event identifier of the attendance to be created
     * @param trackId the track identifier of the attendance to be created
     * @param role    the role of the new attendance
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

        Event event = eventRepository.findById(eventId).get();
        Track track = trackRepository.findById(trackId).orElse(null);
        AppUser user = userRepository.findById(userId).get();

        Attendee attendee = new Attendee(
                new Role(role), new Confirmation(confirmed), event, track, user);

        // Commits the new attendance to the repository
        return attendeeRepository.save(attendee);

    }

    /**
     * Optionally retrieves the attendance instance corresponding to the given
     * identifiers from the database. This method handles the nullable behaviour
     * of the {@code trackId} track identifier.
     *
     * <p>This method is intended to be used
     * internally within the service class; refer to the other public methods
     * within this service class for out-of-class retrieval invocations.
     *
     * @param userId the user identifier to use in retrieval
     * @param eventId the event identifier to use in retrieval
     * @param trackId the track identifier to use in retrieval, potentially nullable
     * @return the optional Attendee instance retrieved from the database
     */
    private Optional<Attendee> findAttendee(Long userId, Long eventId,
                                            @Nullable Long trackId) {
        if (trackId == null) {
            return repository.find(userId, eventId);
        }
        return repository.find(userId, eventId, trackId);
    }

    /**
     * Deletes the attendance corresponding to the provided identifiers
     * if it already exists in the database. Otherwise, if it does not
     * exist, then no deletion takes place, the database remains unmodified,
     * and a {@link NoSuchElementException} is thrown.
     *
     * <p>This is a private method intended to be used within this service
     * class only. For out-of-class invocations, refer to the {@link #remove}
     * method or the {@link #resign} method.
     *
     * @param userId  the user identifier of the instance to be deleted
     * @param eventId the event identifier of the instance to be deleted
     * @param trackId the track identifier of the instance to be deleted,
     *                potentially nullable
     * @throws NoSuchElementException indicates that no such attendance
     *                                can be found, and that no deletion can take
     *                                place.
     */
    @Transactional
    public void deleteAttendance(Long userId, Long eventId, @Nullable Long trackId)
            throws NoSuchElementException {
        Optional<Attendee> retrievedAttendance = attendeeRepository.findByUserIdAndEventIdAndTrackId(userId, eventId,
                trackId);

        // Exception handling for when no attendances can be found.
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found, so no deletion is possible.");
        }

        Attendee attendee = retrievedAttendance.get();

        // Deletes the Attendee instance associated with the given composite key.
        attendeeRepository.delete(attendee);

    }

    // Public Methods

    /**
     * Retrieves the confirmed Attendee instance corresponding to the given
     * identifiers. If the attendance does not exist, or if the attendance
     * is not confirmed, then no instance is returned, and a
     * {@link NoSuchElementException}
     * is thrown.
     *
     * @param userId  the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier
     * @return the retrieved Attendee instance
     * @throws NoSuchElementException indicates that the attendance instance
     *                                does not exist or is unconfirmed.
     */
    public Attendee getAttendance(Long userId, Long eventId, @Nullable Long trackId)
            throws NoSuchElementException {

        Optional<Attendee> retrievedAttendee = attendeeRepository.findByUserIdAndEventIdAndTrackIdAndConfirmation(userId,
            eventId, trackId, new Confirmation(true));

        // Exception handling for when the repository can find the instance
        if (retrievedAttendee.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found.");
        }

        return retrievedAttendee.get();
    }

    /**
     * Retrieves all confirmed attendances corresponding to the given
     * user identifier. If no such attendances exist, then a
     * {@link NoSuchElementException} is thrown.
     *
     * @param userId the user identifier
     * @return the list of corresponding attendances
     * @throws NoSuchElementException indicates that no such attendances
     *                                exist.
     */
    public List<Attendee> getAttendanceByUser(Long userId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = attendeeRepository.findByUserIdAndConfirmation(userId, new Confirmation(true));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this user can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves all confirmed attendances corresponding to the given
     * event identifier. If no such attendances exist, then a
     * {@link NoSuchElementException} is thrown.
     *
     * @param eventId the event identifier
     * @return the list of corresponding attendances
     * @throws NoSuchElementException indicates that no such attendances
     *                                exist.
     */
    public List<Attendee> getAttendanceByEvent(Long eventId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = attendeeRepository.findByEventIdAndConfirmation(eventId, new Confirmation(true));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this event can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves the attendance corresponding to the given
     * identifiers.
     *
     * @param id the Attendee identifier
     * @return corresponding Attendee
     */
    public boolean existsById(Long id) {

        return attendeeRepository.existsById(id);
    }

    /**
     * Retrieves the attendance corresponding to the given
     * identifiers.
     *
     * @param id the Attendee identifier
     */
    public void deleteById(Long id) {
        attendeeRepository.deleteById(id);
    }

    /**
     * Retrieves the attendance corresponding to the given
     * identifiers.
     *
     * @param id the Attendee identifier
     */
    public Attendee getById(Long id) {
        return attendeeRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all confirmed attendances corresponding to the given
     * track identifier. If no such attendances exist, then a
     * {@link NoSuchElementException} is thrown.
     *
     * @param trackId the track identifier, not nullable
     * @return the list of attendances
     * @throws NoSuchElementException indicates that no such attendances
     *                                exist.
     */
    public List<Attendee> getAttendanceByTrack(@NonNull Long trackId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = attendeeRepository.findByTrackIdAndConfirmation(trackId, new Confirmation(true));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this track can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves all unconfirmed Attendee instances. If no such attendance exists,
     * then no instance is returned, and a {@link NoSuchElementException} is thrown.
     *
     * @return the list of corresponding attendances
     * @throws NoSuchElementException indicates that no such attendances exist.
     */
    public List<Attendee> getInvitations()
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.find(new Confirmation(false));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No unconfirmed attendances can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves the unconfirmed Attendee instance corresponding to the given
     * identifiers. If the attendance does not exist, or if the attendance
     * is confirmed, then no instance is returned, and a {@link NoSuchElementException}
     * is thrown.
     *
     * @param userId the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier
     * @return the retrieved Attendee instance
     * @throws NoSuchElementException indicates that the attendance instance
     *          does not exist or is confirmed.
     */
    public Attendee getInvitation(Long userId, Long eventId, Long trackId)
            throws NoSuchElementException {

        Optional<Attendee> retrievedAttendee = findAttendee(userId, eventId, trackId);

        // Exception handling for when the repository can find the instance
        if (retrievedAttendee.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found.");
        }

        // Exception handling for when the attendance is not confirmed (i.e. still an invitation)
        if (retrievedAttendee.get().isConfirmed()) {
            throw new NoSuchElementException("No unconfirmed attendance can be found.");
        }

        return retrievedAttendee.get();
    }

    /**
     * Retrieves all unconfirmed attendances corresponding to the given
     * user identifier. If no such attendances exist, then a
     * {@link NoSuchElementException} is thrown.
     *
     * @param userId the user identifier
     * @return the list of corresponding attendances
     * @throws NoSuchElementException indicates that no such attendances
     *          exist.
     */
    public List<Attendee> getInvitationByUser(Long userId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.findByUser(userId, new Confirmation(false));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No unconfirmed attendance associated with this user can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves all unconfirmed attendances corresponding to the given
     * event identifier. If no such attendances exist, then a
     * {@link NoSuchElementException} is thrown.
     *
     * @param eventId the event identifier
     * @return the list of corresponding attendances
     * @throws NoSuchElementException indicates that no such attendances
     *          exist.
     */
    public List<Attendee> getInvitationByEvent(Long eventId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.findByEvent(eventId, new Confirmation(false));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No unconfirmed attendance associated with this event can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves all unconfirmed attendances corresponding to the given
     * track identifier. If no such attendances exist, then a
     * {@link NoSuchElementException} is thrown.
     *
     * @param trackId the track identifier, not nullable
     * @return the list of attendances
     * @throws NoSuchElementException indicates that no such attendances
     *          exist.
     */
    public List<Attendee> getInvitationByTrack(@NonNull Long trackId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.findByTrack(trackId, new Confirmation(false));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No unconfirmed attendance associated with this track can be found.");
        }

        return retrievedList;
    }


    /**
     * Invites a user by creating an unconfirmed attendance with the specified
     * arguments. If no such executor exists, then a {@link NoSuchElementException}
     * is thrown. If the executor does not have sufficient permission to create the
     * invitation, then a {@link IllegalArgumentException} is thrown.
     *
     * @param executorId the inviter (user) identifier
     * @param userId     the user identifier
     * @param eventId    the event identifier
     * @param trackId    the track identifier
     * @param role       the event role to be conferred
     */
    public void invite(Long executorId, Long userId, Long eventId, Long trackId, RoleTitle role) {

        // TODO: This implementation depends on the implementation of enroll, as no person can be invited
        //  if there is no person who is already attending. For this reason this code is still commented out.
        //  Also, we still need to decide which roles get to invite others, and for which roles.
        /*
        Optional<Attendee> retrievedExecutor = findAttendee(executorId, eventId, trackId);
        if (retrievedExecutor.isEmpty()) {
            throw new NoSuchElementException("No executor with associated userId, eventId, and trackId can be found.");
        }

        Attendee executor = retrievedExecutor.get();
        if (executor.getRole().getRoleTitle().getPermission() > role.getPrecedence()) {
            throw new IllegalArgumentException("Inviter has insufficient permission to create such an invitation.");
        } */

        createAttendance(userId, eventId, trackId, role, false);
        // TODO create an invitation
    }

    /**
     * Accepts an invitation by changing the confirmed attribute to true.
     *
     * @param executorId the executor identifier
     * @param userId     the user identifier
     * @param eventId    the event identifier
     * @param trackId    the track identifier
     */
    @Transactional
    public void accept(Long executorId, Long userId, Long eventId, Long trackId) {

        // Optional<Attendee> retrievedExecutor = findAttendee(executorId, eventId,
        // trackId);
        Optional<Attendee> retrievedAttendance = attendeeRepository.findByUserIdAndEventIdAndTrackId(userId, eventId,
                trackId);

        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No invitation with associated userId, eventId, and trackId can be found.");
        }

        Optional<Attendee> retrievedExecutor = findAttendee(executorId, eventId, trackId);
        if (retrievedExecutor.isEmpty()) {
            throw new NoSuchElementException("No executor with associated userId, eventId, and trackId can be found.");
        }

        Attendee attendee = retrievedAttendance.get();
        attendee.setConfirmation(true);

        attendeeRepository.save(attendee);

    }

    /**
     * Rejects an invitation by removing it from the database.
     *
     * @param executorId the executor (user) identifier
     * @param userId     the user identifier
     * @param eventId    the event identifier
     * @param trackId    the track identifier
     */
    public void reject(Long executorId, Long userId, Long eventId, Long trackId) {

        Optional<Attendee> retrievedAttendance = findAttendee(userId, eventId, trackId);
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No invitation with associated userId, eventId, and trackId can be found.");
        }

        Optional<Attendee> retrievedExecutor = findAttendee(executorId, eventId, trackId);
        if (retrievedExecutor.isEmpty()) {
            throw new NoSuchElementException("No executor with associated userId, eventId, and trackId can be found.");
        }

        Attendee executor = retrievedExecutor.get();
        Attendee attendance = retrievedAttendance.get();

        // TODO: Shouldn't this be handled by auth?
        //  Who gets to REJECT other peoples invites?
        if (!userId.equals(executorId)
            && executor.getRole().getRoleTitle().getPermission() > attendance.getRole().getRoleTitle().getPrecedence()) {
            throw new IllegalArgumentException("Executor has insufficient permission to reject such an invitation.");
        }

        if (attendance.isConfirmed()) {
            throw new IllegalArgumentException("Invitation has already been accepted, use resign or remove instead.");
        }

        repository.delete(attendance);
    }

    /**
     * Self-enroll in an event.
     *
     * @param enroleeId the enrollee (user) identifier
     * @param eventId   the event identifier
     */
    @Transactional
    public void enroll(Long enroleeId, Long eventId, Long trackId, RoleTitle role) {

        createAttendance(enroleeId, eventId, trackId, role, false);
        // TODO self-enroll in an event as an attendee.
    }

    /**
     * Resign from a particular attendance.
     *
     * @param userId  the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier
     */
    public void resign(Long userId, Long eventId, Long trackId) {

        deleteAttendance(userId, eventId, trackId);
        // TODO self-resign from a particular attendance.

    }

    /**
     * Remove an attendee from the event.
     *
     * @param executorId the executor (user) identifier
     * @param userId     the user identifier
     * @param eventId    the event identifier
     * @param trackId    the track identifier
     */
    public void remove(Long executorId, Long userId, Long eventId, Long trackId) {

        deleteAttendance(userId, eventId, trackId);
        // TODO remove a user from a particular attendance.
    }

    /**
     * Modifies the role of the corresponding attendance if it exists. Otherwise,
     * a {@link NoSuchElementException} is thrown to indicate that the attendance
     * does not exist.
     *
     * @param executorId the executor (user) identifier
     * @param id         the attendance identifier
     * @param role       the new role
     * @throws NoSuchElementException indicates that no such attendance
     *                                exists, and, therefore, that no modification
     *                                can take place.
     */
    @Transactional
    public Attendee modifyTitle(Long executorId, Long id, RoleTitle role)
            throws NoSuchElementException {

        // Optional<Attendee> retrievedExecutor = findAttendee(executorId, eventId,
        // trackId);
        Optional<Attendee> retrievedAttendance = attendeeRepository.findById(id);

        // Exception handling for when the repository can find the instance
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No such attendance is found; cannot be modified.");
        }

        Attendee attendee = retrievedAttendance.get();

        attendee.setRole(new Role(role));

        // Commit the changes
        return attendeeRepository.save(attendee);
    }
}
