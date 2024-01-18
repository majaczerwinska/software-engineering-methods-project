package nl.tudelft.sem.template.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.api.AttendeeApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.Attendee;
import nl.tudelft.sem.template.model.Role;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.EventService;
import nl.tudelft.sem.template.services.InvitationService;
import nl.tudelft.sem.template.services.TrackService;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



/**
 * The controller for Attendance-related API calls.
 */
@RestController
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidDuplicateLiterals"})
public class AttendeeController implements AttendeeApi {

    private final transient AuthManager authManager;
    private final transient UserService userService;
    private final transient EventService eventService;
    private final transient TrackService trackService;
    private final transient AttendeeService attendeeService;
    private final transient InvitationService invitationService;


    /**
     * Constructs a new Attendee controller.
     *
     * @param authManager       Spring Security component used to authenticate and
     *                          authorize the user
     * @param attendeeService   a constructor injection for the Attendee Service class.
     * @param userService       a constructor injection for the User Service class.
     * @param eventService      a constructor injection for the Event Service class.
     * @param trackService      a constructor injection for the Track Service class.
     * @param invitationService a constructor injection for the Invitation Service class.
     */
    @Autowired
    public AttendeeController(AuthManager authManager,
                              AttendeeService attendeeService,
                              UserService userService,
                              EventService eventService,
                              TrackService trackService,
                              InvitationService invitationService) {
        this.authManager = authManager;
        this.attendeeService = attendeeService;
        this.userService = userService;
        this.eventService = eventService;
        this.trackService = trackService;
        this.invitationService = invitationService;
    }

    @Override
    @Transactional
    public ResponseEntity<Attendee> createAttendee(Attendee attendee) {
        // Authenticate the requester
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("message",  "Unauthorized access.")
                    .build();
        }

        if (attendee == null                              // Confirm that the Attendee object was parsed correctly
                 || attendee.getUserId() == null          // Confirm correct parsing of user identifier
                 || attendee.getEventId() == null         // Confirm correct parsing of event identifier
                 || attendee.getRole() == null) {         // Confirm correct parsing of the attendance role
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee object was provided.")
                    .build();
        }
        if (eventService.getEventById(attendee.getEventId()) == null     // Confirm that the event exists
               || (attendee.getTrackId() != null                         // If the identifier is supplied
               && !trackService.exists(attendee.getTrackId()))) {        // confirm that the track exists
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Provided identifiers lead to nonexistent objects.")
                    .build();
        }

        // Create a new Attendee instance
        Attendee createdAttendance;
        try {
            createdAttendance = invitationService
                    .enroll(
                            attendee.getUserId(),
                            attendee.getEventId(),
                            attendee.getTrackId(),
                            RoleTitle.valueOf(attendee.getRole().name()))
                    .toModel();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .header("message",  "Attendance already exists.")
                    .build();
        }

        return ResponseEntity
                  .status(HttpStatus.OK)
                  .header("message",  "successful operation")
                  .body(createdAttendance);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteAttendee(Long attendeeId) {

        // Authenticate the requester
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("message",  "Unauthorized access.")
                    .build();
        }

        // Confirm that the correct identifier is supplied
        if (attendeeId == null) {
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee identifier was provided.")
                    .build();
        }
        if (!attendeeService.exists(attendeeId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("message",  "No Attendee instance corresponding to the given identifier can be found.")
                    .build();
        }
        attendeeService.deleteAttendance(attendeeId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("message",  "successful operation")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<Attendee> getAttendeeByID(Long attendeeId) {

        // Authenticate the requester
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("message",  "Unauthorized access.")
                    .build();
        }

        // Confirm that the correct identifier is supplied
        if (attendeeId == null) {
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee identifier was provided.")
                    .build();
        }
        var retrievedAttendee = attendeeService.getAttendance(attendeeId);

        if (retrievedAttendee == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("message",  "No Attendee instance corresponding to the given identifier can be found.")
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("message",  "successful operation")
                .body(retrievedAttendee.toModel());
    }

    @Override
    @Transactional
    public ResponseEntity<List<Attendee>> getFilteredAttendees(Long eventId,
                                                               List<Role> roles,
                                                               Long trackId) {
        // Authenticate the requester
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("message",  "Unauthorized access.")
                    .build();
        }

        // Confirm that the correct identifiers are supplied
        if (eventId == null && roles == null && trackId == null) {
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee identifier was provided.")
                    .build();
        }
        List<Attendee> modelledAttendees = new ArrayList<Attendee>();
        try {
            var retrievedAttendees = attendeeService.getFilteredAttendance(null,
                    eventId,
                    trackId,
                    true);

            if (roles != null && !roles.isEmpty()) {
                retrievedAttendees.removeIf((x) -> !roles
                        .contains(Role.valueOf(x.getRole().getRoleTitle().name())));
                if (retrievedAttendees.isEmpty()) {
                    throw new NoSuchElementException();
                }
            }

            for (var attendee : retrievedAttendees) {
                modelledAttendees.add(attendee.toModel());
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("message",  "No Attendee instance corresponding to the given identifier can be found.")
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("message",  "successful operation")
                .body(modelledAttendees);
    }

    @Override
    @Transactional
    public ResponseEntity<Attendee> updateAttendee(Attendee attendee) {
        // Authenticate the requester
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("message",  "Unauthorized access.")
                    .build();
        }

        if (attendee == null                             // Confirm that the Attendee object was parsed correctly
                || attendee.getId() == null              // Confirm correct parsing of attendance identifier
                || attendee.getRole() == null) {         // Confirm correct parsing of the attendance role
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee object was provided.")
                    .build();
        }
        if (!attendeeService.exists(attendee.getId())) { // confirm that the Attendance exists
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Provided identifiers lead to nonexistent objects.")
                    .build();
        }

        Attendee modifiedAttendee = null;

        try {
            // Modify the Role title if possible
            modifiedAttendee = attendeeService
                    .modifyTitle(user.getId(), attendee.getId(), RoleTitle.valueOf(attendee.getRole().name()))
                    .toModel();
        } catch (IllegalCallerException e) {
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("message",  "No sufficient attendance permission.")
                    .build();
        } catch (IllegalArgumentException e1) {
            ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header("message",  "No sufficient number of general chairs to proceed with this operation.")
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("message",  "successful operation")
                .body(modifiedAttendee);
    }
}
