package nl.tudelft.sem.template.example.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.example.domain.attendee.Attendee;
import nl.tudelft.sem.template.example.domain.attendee.AttendeeId;
import nl.tudelft.sem.template.example.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.example.domain.attendee.Confirmation;
import nl.tudelft.sem.template.example.domain.attendee.Role;
import nl.tudelft.sem.template.example.domain.attendee.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AttendeeService {

    private final transient AttendeeRepository repository;

    /**
     * A constructor dependency injection for the Attendee JPA Repository concrete
     * implementation.
     *
     * @param repository the attendee repository injection
     */
    @Autowired
    public AttendeeService(AttendeeRepository repository) {
        this.repository = repository;
    }


    /**
     * Creates a new unconfirmed attendance instance, which is committed to the
     * attendee repository. In case where such an attendance instance already exists,
     * confirmed or not, the method throws an illegal argument exception to signify
     * that no change has taken place. This method is to be used within the service
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
    private void createAttendance(Long userId, Long eventId, Long trackId, RoleTitle role)
            throws IllegalArgumentException {

        // Check that no such attendance already exists
        if (isInvited(userId, eventId, trackId)) {
            throw new IllegalArgumentException("Attendance instance already exists.");
        }

        Attendee attendee = new Attendee(
                userId,
                eventId,
                trackId,
                new Role(role),
                new Confirmation(false)
        );

        // Commits the new attendance to the repository
        repository.save(attendee);

    }

    /**
     * Verifies that the attendance of a user in the given event and track
     * exists. This method does not discriminate in terms of the confirmation
     * status of the attendance.
     *
     * @param userId the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier
     * @return Returns TRUE if such an attendance exists in the repository.
     *          Otherwise, returns FALSE.
     */
    public boolean isInvited(Long userId, Long eventId, Long trackId) {
        return repository.exists(userId, eventId, trackId);
    }

    /**
     * Verifies that the confirmed attendance of a user in the given event
     * and track exists. This method DOES discriminate in terms of the
     * confirmation status of the attendance (compare to isInvited).
     *
     * @param userId the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier
     * @return Returns TRUE if such a confirmed attendance exists. Otherwise,
     *          returns FALSE.
     */
    public boolean isAttending(Long userId, Long eventId, Long trackId) {
        return repository.existsConfirmed(userId, eventId, trackId);
    }

    /**
     * Retrieves the confirmed Attendee instance corresponding to the given
     * composite key. If the attendance does not exist, or if the attendance
     * is not confirmed, then no instance is returned, and a no such element
     * exception is thrown.
     *
     * @param userId the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier
     * @return the retrieved Attendee instance
     * @throws NoSuchElementException indicates that the attendance instance
     *          does not exist or is unconfirmed.
     */
    public Attendee getAttendance(Long userId, Long eventId, Long trackId)
            throws NoSuchElementException {

        Optional<Attendee> retrievedAttendee = repository.find(userId, eventId, trackId);

        // Exception handling for when the repository can find the instance
        if (retrievedAttendee.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found.");
        }

        // Exception handling for when the attendance is not confirmed (i.e. still an invitation)
        if (!retrievedAttendee.get().getConfirmation().getConfirmed()) {
            throw new NoSuchElementException("No confirmed attendance can be found.");
        }

        return retrievedAttendee.get();
    }

    /**
     * Retrieves all confirmed attendances corresponding to the given
     * user identifier. If no such attendances exist, then the no such
     * element exception is thrown.
     *
     * @param userId the user identifier
     * @return the list of attendances
     * @throws NoSuchElementException indicates that no such attendances
     *          exist.
     */
    public List<Attendee> getAttendanceByUser(Long userId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.findByUser(userId, true);

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this user can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves all confirmed attendances corresponding to the given
     * event identifier. If no such attendances exist, then the no such
     * element exception is thrown.
     *
     * @param eventId the event identifier
     * @return the list of attendances
     * @throws NoSuchElementException indicates that no such attendances
     *          exist.
     */
    public List<Attendee> getAttendanceByEvent(Long eventId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.findByUser(eventId, true);

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this event can be found.");
        }

        return retrievedList;
    }

    /**
     * Retrieves all confirmed attendances corresponding to the given
     * track identifier. If no such attendances exist, then the no such
     * element exception is thrown.
     *
     * @param trackId the user identifier
     * @return the list of attendances
     * @throws NoSuchElementException indicates that no such attendances
     *          exist.
     */
    public List<Attendee> getAttendanceByTrack(Long trackId)
            throws NoSuchElementException {

        List<Attendee> retrievedList = repository.findByUser(trackId, true);

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No confirmed attendance associated with this track can be found.");
        }

        return retrievedList;
    }


    /**
     * Invites a user.
     *
     * @param executorId the inviter (user) identifier
     * @param key the composite primary key
     * @param role the event role to be conferred
     */
    public void invite(Long executorId, AttendeeId key, RoleTitle role) {

        //Optional<Attendee> retrievedExecutor = repository.findById(key);

        createAttendance(key.getUserId(), key.getEventId(), key.getTrackId(), RoleTitle.ATTENDEE);

        //TODO create an invitation

    }

    /**
     * Accepts an invitation.
     *
     * @param executorId the executor identifier
     * @param key the composite primary key.
     */
    public void accept(Long executorId, AttendeeId key) {

        //Optional<Attendee> retrievedAttendance = repository.findById(key);
        //TODO accept an invitation

    }

    /**
     * Rejects an invitation.
     *
     * @param executorId the executor (user) identifier
     * @param key the composite primary key
     */
    public void reject(Long executorId, AttendeeId key) {

        //Optional<Attendee> retrievedAttendance = repository.findById(key);

        //TODO reject an invitation

    }

    /**
     * Self-enroll in an event.
     *
     * @param enroleeId the enrollee (user) identifier
     * @param eventId the event identifier
     */
    public void enroll(Long enroleeId, Long eventId) {

        //TODO self-enroll in an event as an attendee.
        createAttendance(enroleeId, eventId, null, RoleTitle.ATTENDEE);
    }

    /**
     * Resign from a particular attendance.
     *
     * @param key the primary composite key
     */
    public void resign(AttendeeId key) {

        //TODO self-resign from a particular attendance.

    }

    /**
     * Remove an attendee from the event.
     *
     * @param executorId the executor (user) identifier
     * @param key the composite primary key
     */
    public void remove(Long executorId, AttendeeId key) {

        deleteAttendance(key);
        //TODO remove a user from a particular attendance.
    }

    /**
     * If the attendance exists, then this method modifies the role of the
     * attendance to the provided role. Otherwise, a no such element exception
     * is thrown.
     *
     * @param executorId the executor (user) identifier
     * @param key the composite primary key
     * @param role the new role
     * @throws NoSuchElementException indicates that no such attendance
     *          exists, and, therefore, that no modification can take place.
     */
    @Transactional
    public void modifyTitle(Long executorId, AttendeeId key, RoleTitle role)
            throws NoSuchElementException {

        Optional<Attendee> retrievedAttendance = repository.findById(key);

        // Exception handling for when the repository can find the instance
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found.");
        }

        Attendee attendee = retrievedAttendance.get();

        attendee.setRole(new Role(role));

        // Commit the changes
        repository.save(attendee);
    }


    /**
     * If the attendance exists, then the method deletes that attendance
     * from the repository. Otherwise, no deletion takes place, and the
     * no such element exception is thrown. This is a private method and
     * is meant to only be used within the class (compare to remove and
     * resign).
     *
     * @param key the composite primary key
     * @throws NoSuchElementException indicates that no such attendance
     *          can be found, and that no deletion can take place.
     */
    @Transactional
    private void deleteAttendance(AttendeeId key)
            throws NoSuchElementException {
        Optional<Attendee> retrievedAttendance = repository.findById(key);

        // Exception handling for when no attendances can be found.
        if (retrievedAttendance.isEmpty()) {
            throw new NoSuchElementException("No such attendance can be found, so no deletion is possible.");
        }

        Attendee attendee = retrievedAttendance.get();

        // Deletes the Attendee instance associated with the given composite key.
        repository.delete(attendee);

    }


}
