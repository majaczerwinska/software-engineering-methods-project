package nl.tudelft.sem.template.controllers;

import java.util.List;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.api.AttendeeApi;
import nl.tudelft.sem.template.authentication.AuthManager;
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
@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
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
        if (attendee == null                             // Confirm that the Attendee object was parsed correctly
                 || attendee.getUserId() == null          // Confirm correct parsing of user identifier
                 || attendee.getEventId() == null         // Confirm correct parsing of event identifier
                 || attendee.getRole() == null) {         // Confirm correct parsing of the attendance role
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee object was provided.")
                    .build();
        }
        if (userService.getUserById(attendee.getId()) == null                   // Confirm that the user exists
               || eventService.getEventById(attendee.getEventId()) == null      // Confirm that the event exists
               || (attendee.getTrackId() != null                                // If the identifier is supplied
               && trackService.trackExistById(attendee.getTrackId()))) {        // confirm that the track exists
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Provided identifiers lead to nonexistent objects.")
                    .build();
        }

        // Create a new Attendee instance
        Attendee createdAttendance = attendeeService
                  .createAttendance(
                         attendee.getUserId(),
                         attendee.getEventId(),
                         attendee.getTrackId(),
                         RoleTitle.valueOf(attendee.getRole().name()),
                         true)
                  .toModel();

        return ResponseEntity
                  .status(HttpStatus.OK)
                  .header("message",  "successful operation")
                  .body(createdAttendance);
    }

    @Override
    public ResponseEntity<Void> deleteAttendee(Long attendeeId) {

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
    public ResponseEntity<Attendee> getAttendeeByID(Long attendeeId) {
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
    public ResponseEntity<List<Attendee>> getInvitesByEventID(Long eventId, List<Role> roles, Long trackId) {
        return AttendeeApi.super.getInvitesByEventID(eventId, roles, trackId);
    }

    @Override
    public ResponseEntity<Attendee> updateAttendee(Attendee attendee) {
        if (attendee == null                             // Confirm that the Attendee object was parsed correctly
                || attendee.getId() == null              // Confirm correct parsing of attendance identifier
                || attendee.getUserId() == null          // Confirm correct parsing of user identifier
                || attendee.getEventId() == null         // Confirm correct parsing of event identifier
                || attendee.getRole() == null) {         // Confirm correct parsing of the attendance role
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Invalid attendee object was provided.")
                    .build();
        }
        if (attendeeService.exists(attendee.getId())) { // confirm that the Attendance exists
            // Otherwise it is a bad request.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message",  "Provided identifiers lead to nonexistent objects.")
                    .build();
        }

        // Modify the Role title if necessary
        Attendee modifiedAttendee = attendeeService
                .modifyTitle(attendee.getId(), RoleTitle.valueOf(attendee.getRole().name()))
                .toModel();


        return ResponseEntity
                .status(HttpStatus.OK)
                .header("message",  "successful operation")
                .body(modifiedAttendee);
    }
}
