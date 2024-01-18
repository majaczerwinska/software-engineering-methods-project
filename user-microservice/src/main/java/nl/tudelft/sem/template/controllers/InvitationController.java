package nl.tudelft.sem.template.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.api.InvitationsApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.Invitation;
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
public class InvitationController implements InvitationsApi {

	private final transient AuthManager authManager;
	private final transient UserService userService;
	private final transient EventService eventService;
	private final transient TrackService trackService;
	private final transient AttendeeService attendeeService;
	private final transient InvitationService invitationService;


	/**
	 * Constructs a new Invitation controller.
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
	public InvitationController(AuthManager authManager,
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
	public ResponseEntity<Invitation> acceptInvitation(Integer invitationId) {
		if (invitationId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		}

		AppUser executor = userService.getUserByEmail(new Email(authManager.getEmail()));
		if (executor == null) {
			// Executor does not exist.
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
		}

		if (!attendeeService.exists(invitationId.longValue())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
		}

		try {
			Invitation invitation = invitationService.accept(executor.getId(), new Long(invitationId))
										.toInvitationModel();
			return ResponseEntity.ok(invitation); // 200
		} catch (IllegalArgumentException e) {
			// Executor does not have permission to accept attendance.
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
		}


	}

	@Override
	public ResponseEntity<Invitation> createInvitation(Invitation invitation) {
		if (invitation == null                             // Confirm that the Attendee object was parsed correctly
			|| invitation.getUserId() == null          // Confirm correct parsing of user identifier
			|| invitation.getEventId() == null         // Confirm correct parsing of event identifier
			|| invitation.getRole() == null            // Confirm correct parsing of the attendance role
			|| userService.getUserById(invitation.getUserId()) == null       // Confirm that the user exists
			|| eventService.getEventById(invitation.getEventId()) == null    // Confirm that the event exists
			|| (invitation.getTrackId() != null                              // If the identifier is supplied
			&& !trackService.exists(invitation.getTrackId()))) { 	 // Confirm that the track exists
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		}

		AppUser executor = userService.getUserByEmail(new Email(authManager.getEmail()));
		if (executor == null) {
			// Executor does not exist.
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
		}

		try {
			invitationService.invite(executor.getId(), invitation.getUserId(), invitation.getEventId(),
					invitation.getTrackId(), RoleTitle.valueOf(invitation.getRole().name()));
		}  catch (NoSuchElementException e) {
			// Executor doesn't attend.
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
		} catch (IllegalArgumentException e) {
			// Attendance already exists.
			return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
		}

		return ResponseEntity.ok(invitation); // 200
	}


	@Override
	public ResponseEntity<Void> deleteInvitation(Integer invitationId) {
		if (invitationId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		}

		AppUser executor = userService.getUserByEmail(new Email(authManager.getEmail()));
		if (executor == null) {
			// Executor does not exist.
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
		}

		if (!attendeeService.exists(invitationId.longValue())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
		}

		try {
			invitationService.remove(executor.getId(), new Long(invitationId));
		} catch (IllegalArgumentException e) {
			// Executor does not have permission to accept attendance.
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
	}

	@Override
	public ResponseEntity<Invitation> getInvitation(Integer invitationId) {
		if (invitationId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		}

		nl.tudelft.sem.template.domain.attendee.Attendee attendee = attendeeService.getAttendance(
				new Long(invitationId));
		if (attendee == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
		}

		return ResponseEntity.ok(attendee.toInvitationModel()); // 200
	}

	@Override
	public ResponseEntity<List<Invitation>> getInvitations(Long userId, Long trackId, Long eventId) {
		if ((userId != null && userService.getUserById(userId) == null)
				|| (eventId != null && eventService.getEventById(eventId) == null)
				|| (trackId != null && !trackService.exists(trackId))) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		}

		List<Invitation> invitations = attendeeService.getFilteredAttendance(userId, eventId, trackId, false)
				.stream()
				.map(nl.tudelft.sem.template.domain.attendee.Attendee::toInvitationModel)
				.collect(Collectors.toList());

		return ResponseEntity.ok(invitations);
	}
}
