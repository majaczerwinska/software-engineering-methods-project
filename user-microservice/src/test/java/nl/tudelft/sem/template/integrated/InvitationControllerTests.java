package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.controllers.InvitationController;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.track.Description;
import nl.tudelft.sem.template.domain.track.PaperRequirement;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.Invitation;
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.model.Role;
import nl.tudelft.sem.template.services.InvitationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class InvitationControllerTests {

	@MockBean
	private AuthManager authManager;

	@Autowired
	private transient EventRepository eventRepository;

	@Autowired
	private transient AttendeeRepository attendeeRepository;

	@Autowired
	private transient UserRepository userRepository;

	@Autowired
	private transient TrackRepository trackRepository;

	@Autowired
	@InjectMocks
	private transient InvitationController invitationController;

	@Autowired
	private transient InvitationService invitationService;


	private Event event;
	private AppUser user;
	private AppUser exec;
	private Track track;

	/**
	 * Setup for each test.
 	 */
	@BeforeEach
	public void setup() {
		LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
		LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
		user = new AppUser(new Email("test@test.net"), new Name("name"), new Name("name2"));
		exec = new AppUser(new Email("best@test.net"), new Name("name2"), new Name("name2"));
		event = new Event(startDate, endDate, new IsCancelled(false), new EventName("name"),
			new EventDescription("desc"));
		track = new Track(new Title("title"), new Description("desc"), new PaperRequirement(PaperType.FULL_PAPER),
				startDate, endDate, event);
		when(authManager.getEmail()).thenReturn("best@test.net");
	}

	@Test
	public void getInvitationsBadRequestTest1() {
		ResponseEntity<List<Invitation>> response = invitationController.getInvitations(1L, null, null);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void getInvitationsBadRequestTest2() {
		ResponseEntity<List<Invitation>> response = invitationController.getInvitations(null, 1L, null);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void getInvitationsBadRequestTest3() {
		ResponseEntity<List<Invitation>> response = invitationController.getInvitations(null, null, 1L);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void getInvitationsTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		invitationService.enroll(user.getId(), event.getId(), track.getId(), RoleTitle.PC_CHAIR);
		invitationService.enroll(exec.getId(), event.getId(), track.getId(), RoleTitle.PC_CHAIR);
		ResponseEntity<List<Invitation>> response = invitationController.getInvitations(null, null, event.getId());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Invitation> invitations = response.getBody();
		assertEquals(2, invitations.size());
	}

	@Test
	public void createInvitationBadRequestTest1() {
		ResponseEntity<Invitation> response = invitationController.createInvitation(null);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationBadRequestTest2() {
		Invitation invitation = new Invitation();

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationBadRequestTest3() {
		Invitation invitation = new Invitation();
		invitation.setUserId(99L);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationBadRequestTest4() {
		Invitation invitation = new Invitation();
		invitation.setUserId(99L);
		invitation.setEventId(99L);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationBadRequestTest5() {
		Invitation invitation = new Invitation();
		invitation.setUserId(99L);
		invitation.setEventId(99L);
		invitation.setRole(Role.PC_CHAIR);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationBadRequestTest6() {
		userRepository.save(user);

		Invitation invitation = new Invitation();
		invitation.setUserId(user.getId());
		invitation.setEventId(99L);
		invitation.setRole(Role.PC_CHAIR);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationBadRequestTest7() {
		userRepository.save(user);
		eventRepository.save(event);

		Invitation invitation = new Invitation();
		invitation.setUserId(user.getId());
		invitation.setEventId(event.getId());
		invitation.setTrackId(99L);
		invitation.setRole(Role.PC_CHAIR);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createInvitationUnauthorizedTest() {
		userRepository.save(user);
		eventRepository.save(event);
		trackRepository.save(track);

		Invitation invitation = new Invitation();
		invitation.setUserId(user.getId());
		invitation.setEventId(event.getId());
		invitation.setTrackId(track.getId());
		invitation.setRole(Role.PC_CHAIR);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void createInvitationUnauthorizedTest2() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Invitation invitation = new Invitation();
		invitation.setUserId(user.getId());
		invitation.setEventId(event.getId());
		invitation.setTrackId(track.getId());
		invitation.setRole(Role.PC_CHAIR);

		ResponseEntity<Invitation> response = invitationController.createInvitation(invitation);
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void createInvitationConflictTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Invitation invitation = new Invitation();
		invitation.setUserId(user.getId());
		invitation.setEventId(event.getId());
		invitation.setTrackId(track.getId());
		invitation.setRole(Role.ATTENDEE);

		Attendee execAttendee = invitationService.enroll(exec.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		invitationService.accept(exec.getId(), execAttendee.getId());

		ResponseEntity<Invitation> response1 = invitationController.createInvitation(invitation);
		ResponseEntity<Invitation> response2 = invitationController.createInvitation(invitation);
		assertEquals(response2.getStatusCode(), HttpStatus.CONFLICT);
	}

	@Test
	public void createInvitationTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Invitation invitation = new Invitation();
		invitation.setUserId(user.getId());
		invitation.setEventId(event.getId());
		invitation.setTrackId(track.getId());
		invitation.setRole(Role.ATTENDEE);

		Attendee execAttendee = invitationService.enroll(exec.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		invitationService.accept(exec.getId(), execAttendee.getId());

		ResponseEntity<Invitation> response1 = invitationController.createInvitation(invitation);
		assertEquals(response1.getStatusCode(), HttpStatus.OK);
		Invitation invitation1 = response1.getBody();
		assertEquals(invitation1.getUserId(), user.getId());
		assertEquals(invitation1.getEventId(), event.getId());
		assertEquals(invitation1.getTrackId(), track.getId());
		assertEquals(invitation1.getRole(), Role.ATTENDEE);
	}

	@Test
	public void getInvitationBadRequestTest() {
		ResponseEntity<Invitation> response = invitationController.getInvitation(null);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void getInvitationNotFoundTest() {
		ResponseEntity<Invitation> response = invitationController.getInvitation(1);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void getInvitationTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Attendee invitation = invitationService.enroll(user.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		ResponseEntity<Invitation> response = invitationController.getInvitation(
			Math.toIntExact(invitation.getId()));
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void deleteInvitationBadRequestTest() {
		ResponseEntity<Void> response = invitationController.deleteInvitation(null);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void deleteInvitationUnauthorizedTest1() {
		ResponseEntity<Void> response = invitationController.deleteInvitation(1);
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void deleteInvitationUnauthorizedTest2() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Attendee invitation = invitationService.enroll(user.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		ResponseEntity<Void> response = invitationController.deleteInvitation(Math.toIntExact(invitation.getId()));
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void deleteInvitationNotFoundTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		ResponseEntity<Void> response = invitationController.deleteInvitation(1);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void deleteInvitationTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Attendee invitation = invitationService.enroll(exec.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		ResponseEntity<Void> response = invitationController.deleteInvitation(Math.toIntExact(invitation.getId()));
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
	}

	@Test
	public void acceptInvitationBadRequestTest() {
		ResponseEntity<Invitation> response = invitationController.acceptInvitation(null);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void acceptInvitationUnauthorizedTest1() {
		ResponseEntity<Invitation> response = invitationController.acceptInvitation(1);
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void acceptInvitationUnauthorizedTest2() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Attendee invitation = invitationService.enroll(user.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		ResponseEntity<Invitation> response = invitationController.acceptInvitation(
			Math.toIntExact(invitation.getId()));
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void acceptInvitationNotFoundTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		ResponseEntity<Invitation> response = invitationController.acceptInvitation(1);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void acceptInvitationTest() {
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);

		Attendee invitation = invitationService.enroll(exec.getId(), event.getId(), track.getId(),
			RoleTitle.PC_CHAIR);
		ResponseEntity<Invitation> response = invitationController.acceptInvitation(
			Math.toIntExact(invitation.getId()));
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Invitation invitation1 = response.getBody();
		assertEquals(invitation1.getUserId(), exec.getId());
		assertEquals(invitation1.getEventId(), event.getId());
		assertEquals(invitation1.getTrackId(), track.getId());
		assertEquals(invitation1.getRole(), Role.PC_CHAIR);
	}
}
