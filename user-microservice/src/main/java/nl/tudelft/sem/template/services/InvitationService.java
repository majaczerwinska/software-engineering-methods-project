package nl.tudelft.sem.template.services;

import java.util.List;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
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
    private final transient AttendeeLogFactory attendeeLogFactory;

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
        attendeeLogFactory = (AttendeeLogFactory) LogFactory.loadFactory(LogType.ATTENDEE);
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
    public Attendee invite(Long executorId, Long userId, Long eventId, Long trackId, RoleTitle role) {

        List<Attendee> executors = attendeeService.getFilteredAttendance(executorId, eventId, trackId, true);
        if (executors.get(0).getRole().getRoleTitle().getPermission() > role.getPrecedence()) {
            throw new NoSuchElementException("Inviter has insufficient permission to create the invitation.");
        }

        return attendeeService.createAttendance(userId, eventId, trackId, role, false);
    }

    /**
     * Accepts an invitation by changing the confirmed attribute to true.
     *
     * @param executorId the executor identifier
     * @param subjectId  the invitation identifier
     */
    @Transactional
    public Attendee accept(Long executorId, Long subjectId) {

        if (!attendeeService.suffices(executorId, subjectId)) {
            throw new IllegalArgumentException("Inviter has insufficient permission to accept the invitation.");
        }

        Attendee retrievedSubject = attendeeService.getAttendance(subjectId);
        retrievedSubject.setConfirmation(true);
        attendeeLogFactory.registerConfirmationChange(retrievedSubject);
        return attendeeRepository.save(retrievedSubject);
    }

    /**
     * Rejects an invitation by removing it from the database.
     *
     * @param executorId the executor identifier
     * @param subjectId  the invitation identifier
     */
    public void reject(Long executorId, Long subjectId) {

        Attendee attendee = attendeeService.getAttendance(subjectId);
        if (attendee == null) {
            throw new IllegalArgumentException("Invitation does not exist.");
        }
        if (!executorId.equals(attendee.getUser().getId())) {
            throw new IllegalArgumentException("Inviter has insufficient permission to reject the invitation.");
        }

        attendeeService.deleteAttendance(subjectId);
    }

    /**
     * Removes an invitation by removing it from the database.
     *
     * @param executorId the executor identifier
     * @param subjectId  the invitation identifier
     */
    public void remove(Long executorId, Long subjectId) {

        if (!attendeeService.suffices(executorId, subjectId)) {
            throw new IllegalArgumentException("Inviter has insufficient permission to remove the invitation.");
        }

        attendeeService.deleteAttendance(subjectId);
    }

    /**
     * Self-enroll in an event, FOR TESTING ONLY.
     *
     * @param userId     the user identifier
     * @param eventId    the event identifier
     * @param trackId    the track identifier
     * @param role       the event role to be conferred
     */
    @Transactional
    public Attendee enroll(Long userId, Long eventId, Long trackId, RoleTitle role) {

        return attendeeService.createAttendance(userId, eventId, trackId, role, false);

    }






}
