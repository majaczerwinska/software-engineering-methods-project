package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import javax.naming.NoPermissionException;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
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
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.InvitationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain
// beans.
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class InvitationServiceTests {

	@Autowired
	private transient AttendeeRepository attendeeRepository;

	@Autowired
	private transient AttendeeService attendeeService;

	@Autowired
	private transient InvitationService invitationService;

	@Autowired
	private transient UserRepository userRepository;

	@Autowired
	private transient EventRepository eventRepository;

	@Autowired
	private transient TrackRepository trackRepository;

	static AppUser user;
	static AppUser exec;
	static Track track;
	static Event event;

	/**
	 * Initialize globals.
	 */
	@BeforeEach
	public void setup() {
		LocalDate date0 = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
		LocalDate date1 = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
		user = new AppUser(new Email("test@test.test"), new Name("name"), new Name("name"), null, null, null);
		exec = new AppUser(new Email("test@test.test2"), new Name("name"), new Name("name2"), null, null, null);
		event = new Event(date0, date1, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
		track = new Track(new Title("title"), new Description("desc"), new PaperRequirement(PaperType.FULL_PAPER),
				date0, date1, event);
	}

	@Test
	public void enrollTest() {

		// Given
		userRepository.save(user);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle role = RoleTitle.AUTHOR;

		// When
		Attendee attendee1 = invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
        Attendee attendee2 = attendeeService.getAttendance(attendee1.getId());

		// Then
		assertEquals(attendee1, attendee2);
		assertEquals(attendee1.getTrack().getId(), track.getId());
		assertEquals(attendee1.getEvent().getId(), event.getId());
		assertEquals(attendee1.getUser().getId(), user.getId());
		assertEquals(attendee1.getRole().getRoleTitle(), role);
		assertFalse(attendee1.getConfirmation().isConfirmed());

	}

	@Test
	public void acceptNoPermissionTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.PC_CHAIR;
		RoleTitle execRole = RoleTitle.ATTENDEE;

		// When
		Attendee userAttendee = invitationService.enroll(user.getId(), event.getId(), track.getId(), userRole);

		// Then
		assertThrows(IllegalArgumentException.class, () -> invitationService.accept(exec.getId(),
			userAttendee.getId()));

	}

	@Test
	public void acceptTest() {

		// Given
		userRepository.save(user);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle role = RoleTitle.PC_CHAIR;

		// When
		Attendee userAttendee = invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
		userAttendee = invitationService.accept(user.getId(), userAttendee.getId());

		// Then
		assertEquals(userAttendee.getTrack().getId(), track.getId());
		assertEquals(userAttendee.getEvent().getId(), event.getId());
		assertEquals(userAttendee.getUser().getId(), user.getId());
		assertEquals(userAttendee.getRole().getRoleTitle(), role);
		assertTrue(userAttendee.getConfirmation().isConfirmed());
	}

	@Test
	public void removeNoPermissionTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.PC_CHAIR;
		RoleTitle execRole = RoleTitle.ATTENDEE;

		// When
		Attendee userAttendee = invitationService.enroll(user.getId(), event.getId(), track.getId(), userRole);

		// Then
		assertThrows(IllegalArgumentException.class, () -> invitationService.remove(exec.getId(),
			userAttendee.getId()));

	}

	@Test
	public void removeTest() {

		// Given
		userRepository.save(user);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle role = RoleTitle.PC_CHAIR;

		// When
		Attendee userAttendee = invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
		invitationService.remove(user.getId(), userAttendee.getId());

		// Then
		assertNull(attendeeService.getAttendance(userAttendee.getId()));

	}

	@Test
	public void rejectNoExistTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.PC_CHAIR;
		RoleTitle execRole = RoleTitle.ATTENDEE;

		// When - Then
		assertThrows(IllegalArgumentException.class, () -> invitationService.reject(exec.getId(), 1L));

	}

	@Test
	public void rejectNoPermissionTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.PC_CHAIR;
		RoleTitle execRole = RoleTitle.ATTENDEE;

		// When
		Attendee userAttendee = invitationService.enroll(user.getId(), event.getId(), track.getId(), userRole);

		// Then
		assertThrows(IllegalArgumentException.class, () -> invitationService.reject(exec.getId(),
			userAttendee.getId()));

	}

	@Test
	public void rejectTest() {
		// Given
		userRepository.save(user);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle role = RoleTitle.PC_CHAIR;

		// When
		Attendee userAttendee = invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
		invitationService.reject(user.getId(), userAttendee.getId());

		// Then
		assertNull(attendeeService.getAttendance(userAttendee.getId()));
	}


	@Test
	public void inviteNoAttendExecTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.PC_CHAIR;
		RoleTitle execRole = RoleTitle.ATTENDEE;

		// When-then
		assertThrows(NoSuchElementException.class, () -> invitationService.invite(exec.getId(), user.getId(),
				event.getId(), track.getId(), userRole));
	}

	@Test
	public void inviteNoPermissionTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.PC_CHAIR;
		RoleTitle execRole = RoleTitle.ATTENDEE;

		// When
		Attendee execAttendee = invitationService.enroll(exec.getId(), event.getId(), track.getId(), execRole);
		invitationService.accept(exec.getId(), execAttendee.getId());

		// Then
		assertThrows(NoSuchElementException.class, () -> invitationService.invite(exec.getId(), user.getId(),
				event.getId(), track.getId(), userRole));
	}

	@Test
	public void inviteTest() {

		// Given
		userRepository.save(user);
		userRepository.save(exec);
		eventRepository.save(event);
		trackRepository.save(track);
		RoleTitle userRole = RoleTitle.ATTENDEE;
		RoleTitle execRole = RoleTitle.PC_CHAIR;

		// When
		Attendee execAttendee = invitationService.enroll(exec.getId(), event.getId(), track.getId(), execRole);
		invitationService.accept(exec.getId(), execAttendee.getId());
		Attendee userAttendee = invitationService.invite(exec.getId(), user.getId(), event.getId(), track.getId(),
			userRole);
		Attendee userAttendee2 = attendeeService.getAttendance(userAttendee.getId());

		// Then
		assertEquals(userAttendee, userAttendee2);
		assertEquals(userAttendee.getUser().getId(), user.getId());
		assertEquals(userAttendee.getEvent().getId(), event.getId());
		assertEquals(userAttendee.getTrack().getId(), track.getId());
		assertEquals(userAttendee.getRole().getRoleTitle(), userRole);
		assertFalse(userAttendee.getConfirmation().isConfirmed());
	}

}
