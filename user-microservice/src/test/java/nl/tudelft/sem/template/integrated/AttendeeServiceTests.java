package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
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
import org.junit.jupiter.api.BeforeAll;
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
public class AttendeeServiceTests {

    @Autowired
    private transient AttendeeRepository attendeeRepository;

    @Autowired
    private transient AttendeeService service;

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private transient EventRepository eventRepository;

    @Autowired
    private transient TrackRepository trackRepository;

    static AppUser user;
    static Track track;
    static Event event;

    /**
     * Initialize globals.
     */
    @BeforeAll
    public static void setup() {
        LocalDate date0 = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate date1 = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        user = new AppUser(new Email("test@test.test"), new Name("name"), null, null, null);
        event = new Event(date0, date1, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        track = new Track(new Title("title"), new Description("desc"), new PaperRequirement(PaperType.FULL_PAPER),
                date0, date1, event);
    }

    @Test
    public void createAttendeeTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);

        // Self-enroll for the first time
        // When
        service.enroll(user.getId(), event.getId(), null, RoleTitle.ATTENDEE);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), null));

        // Self-enroll for the second time throws exception on duplicate creation
        // When & Then
        var e = assertThrows(IllegalArgumentException.class, () -> {
            service.enroll(user.getId(), event.getId(), null, RoleTitle.ATTENDEE);
        });

        assertEquals(e.getMessage(), "Attendance instance already exists.");
    }

    @Test
    @Transactional
    public void findAttendeeTest() {
        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());
        Attendee attendee = service.getAttendance(user.getId(), event.getId(),
                track.getId());

        // Then
        assertEquals(attendee.getUser(), user);
        assertEquals(attendee.getEvent(), event);
        assertEquals(attendee.getTrack(), track);
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), role);
    }

    @Test
    @Transactional
    public void findAttendeeNullTrackTest() {
        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), null,
                role);
        service.accept(user.getId(), user.getId(), event.getId(), null);
        Attendee attendee = service.getAttendance(user.getId(), event.getId(),
                null);

        // Then
        assertEquals(attendee.getUser().getId(), user.getId());
        assertEquals(attendee.getUser(), user);
        assertEquals(attendee.getEvent(), event);
        assertNull(attendee.getTrack());
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), role);
    }

    @Test
    public void nonExistentRetrievalGetAttendanceTest() {

        // When-Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.getAttendance(1L, 1L, 1L);
        });
        assertEquals(e.getMessage(), "No such attendance can be found.");
    }

    @Test
    public void unconfirmedRetrievalGetAttendanceTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.getAttendance(user.getId(), event.getId(), track.getId());
        });
    }

    @Test
    public void unconfirmedIsInvitedTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), track.getId()));
        assertFalse(
                attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId() + 1L, event.getId(), track.getId()));
    }

    @Test
    public void nullTrackIdIsInvitedTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), track.getId()));
    }

    @Test
    public void changedConfirmationIsAttendingTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
                track.getId(), new Confirmation(true)));

        // When
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
                track.getId(), new Confirmation(true)));
    }

    @Test
    public void nullTrackIdIsAttendingTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
                track.getId(), new Confirmation(true)));

        // When
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
                track.getId(), new Confirmation(true)));
    }

    @Test
    public void modifyTitleTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;
        RoleTitle modifiedRole = RoleTitle.ATTENDEE;

        // When - Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.modifyTitle(user.getId(), user.getId(), event.getId(), track.getId(), modifiedRole);
        });
        assertEquals(e.getMessage(), "No such attendance is found; cannot be modified.");

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());
        service.modifyTitle(user.getId(), user.getId(), event.getId(), track.getId(), modifiedRole);

        Attendee attendee = service.getAttendance(user.getId(), event.getId(), track.getId());

        // Then
        assertEquals(attendee.getRole().getRoleTitle(), modifiedRole);
    }

    @Test
    public void deleteAttendanceTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;

        // When - Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.resign(user.getId(), event.getId(), track.getId());
        });
        assertEquals(e.getMessage(), "No such attendance can be found, so no deletion is possible.");

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());
        service.resign(user.getId(), event.getId(), track.getId());

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), track.getId()));
    }
}
