package nl.tudelft.sem.template.services;

import java.util.List;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.NonNull;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * A service Class handling invitation.
 */
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
@Service
public class InvitationService {

    private final transient AttendeeRepository attendeeRepository;
    private final transient UserRepository userRepository;
    private final transient EventRepository eventRepository;
    private final transient TrackRepository trackRepository;
    private final transient AttendeeService attendeeService;

    /**
     * A constructor dependency injection for the various JPA Repositories.
     *
     * @param attendeeRepository    the attendee repository injection
     * @param userRepository        the user repository injection
     * @param eventRepository       the event repository injection
     * @param trackRepository       the track repository injection
     * @param attendeeService       the attendance service injection
     */
    @Autowired
    public InvitationService(AttendeeRepository attendeeRepository, UserRepository userRepository,
                             EventRepository eventRepository, TrackRepository trackRepository,
                             AttendeeService attendeeService) {
        this.attendeeRepository = attendeeRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.trackRepository = trackRepository;
        this.attendeeService = attendeeService;
    }



    /**
     * Optionally retrieves the attendance instance corresponding to the given
     * identifiers from the database. This method handles the nullable behaviour
     * of the {@code trackId} track identifier.
     *
     * @param userId the user identifier to use in retrieval
     * @param eventId the event identifier to use in retrieval
     * @param trackId the track identifier to use in retrieval, potentially nullable
     * @param confirmed the confirmation status desirable. If null, not filtering is done.
     * @return the optional Attendee instance retrieved from the database
     * @throws NoSuchElementException if the user, event, track, or attendance exist.
     */
    public Attendee getAttendee(Long userId, Long eventId, Long trackId, Boolean confirmed)
            throws NoSuchElementException {

        // Retrieve the attendance objects
        AppUser user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        Track track = null;
        if (trackId != null) {
            track = trackRepository.findById(trackId).orElseThrow(NoSuchElementException::new);
        }
        Confirmation confirmation = (confirmed == null) ? null : new Confirmation(confirmed);

        return attendeeRepository.findByUserAndEventAndTrackAndConfirmation(user, event, track, confirmation)
                .orElseThrow(NoSuchElementException::new);
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

        List<Attendee> retrievedList = attendeeRepository.findByConfirmation(new Confirmation(false));

        // Exception handling for when no attendances can be found.
        if (retrievedList.isEmpty()) {
            throw new NoSuchElementException("No unconfirmed attendances can be found.");
        }

        return retrievedList;
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

        return attendeeService
                .getFilteredAttendance(userId, null, null, false);
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

        return attendeeService
                .getFilteredAttendance(null, eventId, null, false);
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

        return attendeeService
                .getFilteredAttendance(null, null, trackId, false);
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

        attendeeService.createAttendance(userId, eventId, trackId, role, false);
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

        // TODO See suffices(executorId, subjectId) in the AttendeeService
        Attendee retrievedSubject = getAttendee(userId, eventId, trackId, false);


        Attendee retrievedExecutor = getAttendee(executorId, eventId, trackId, true);


        retrievedSubject.setConfirmation(true);

        attendeeRepository.save(retrievedSubject);

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

        // TODO See suffices(executorId, subjectId) in the AttendeeService

        Attendee retrievedSubject = getAttendee(userId, eventId, trackId, false);


        Attendee retrievedExecutor = getAttendee(executorId, eventId, trackId, true);



        // TODO: Shouldn't this be handled by auth?
        //  Who gets to REJECT other peoples invites?
        if (!userId.equals(executorId)
                && retrievedExecutor.getRole().getRoleTitle().getPermission()
                > retrievedSubject.getRole().getRoleTitle().getPrecedence()) {
            throw new IllegalArgumentException("Executor has insufficient permission to reject such an invitation.");
        }

        if (retrievedSubject.isConfirmed()) {
            throw new IllegalArgumentException("Invitation has already been accepted, use resign or remove instead.");
        }

        attendeeService.deleteAttendance(retrievedSubject.getId());
    }

    /**
     * Self-enroll in an event.
     *
     * @param enroleeId the enrollee (user) identifier
     * @param eventId   the event identifier
     */
    @Transactional
    public Attendee enroll(Long enroleeId, Long eventId, Long trackId, RoleTitle role) {

        return attendeeService.createAttendance(enroleeId, eventId, trackId, role, true);
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
        Attendee retrievedSubject = getAttendee(userId, eventId, trackId, null);
        attendeeService.deleteAttendance(retrievedSubject.getId());
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
        // TODO add executor-subject logic here since remove is done by others other than the userId
        // TODO See suffices(executorId, subjectId) in the AttendeeService
        Attendee retrievedSubject = getAttendee(userId, eventId, trackId, null);
        attendeeService.deleteAttendance(retrievedSubject.getId());
        // TODO remove a user from a particular attendance.
    }






}
