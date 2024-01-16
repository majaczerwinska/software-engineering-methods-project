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
        invitationService.enroll(user.getId(), event.getId(), null, RoleTitle.ATTENDEE);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), null));

        // Self-enroll for the second time throws exception on duplicate creation
        // When & Then
        var e = assertThrows(IllegalArgumentException.class, () -> {
            invitationService.enroll(user.getId(), event.getId(), null, RoleTitle.ATTENDEE);
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
        invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
        Attendee attendee = invitationService.getAttendee(user.getId(), event.getId(),
                track.getId(), true);

        // Then
        assertEquals(attendee.getUser(), user);
        assertEquals(attendee.getEvent(), event);
        assertEquals(attendee.getTrack(), track);
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), role);
        assertThrows(NoSuchElementException.class, () -> {
            invitationService.getAttendee(user.getId(), event.getId(), track.getId(), false);
        });
    }

    //    @Test
    //    public void findAttendeeNullTrackTest() {
    //        // Given
    //        user = userRepository.save(user);
    //        event = eventRepository.save(event);
    //        RoleTitle role = RoleTitle.ATTENDEE;
    //
    //        // When
    //        invitationService.enroll(user.getId(), event.getId(), null, role);
    //        Attendee attendee = invitationService.getAttendee(user.getId(), event.getId(),
    //                null, true);
    //
    //        // Then
    //        assertEquals(attendee.getUser().getId(), user.getId());
    //        assertEquals(attendee.getUser(), user);
    //        assertEquals(attendee.getEvent(), event);
    //        assertNull(attendee.getTrack());
    //        assertTrue(attendee.getConfirmation().isConfirmed());
    //        assertEquals(attendee.getRole().getRoleTitle(), role);
    //    }

    //    @Test
    //    public void nonExistentRetrievalGetAttendanceTest() {
    //
    //        // When
    //        RoleTitle role = RoleTitle.ATTENDEE;
    //        invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
    //
    //        // When-Then
    //        assertThrows(IllegalArgumentException.class, () -> {
    //            invitationService.getAttendee(-1L, -1L, -1L, null);
    //        });
    //        assertThrows(IllegalArgumentException.class, () -> {
    //            invitationService.getAttendee(user.getId(), -1L, -1L, null);
    //        });
    //        assertThrows(IllegalArgumentException.class, () -> {
    //            invitationService.getAttendee(user.getId(), event.getId(), -1L, null);
    //        });
    //    }



    @Test
    public void nonExistentRetrievalGetInvitationTest() {

        // When-Then
        assertThrows(NoSuchElementException.class, () -> {
                invitationService.getInvitations();
            }
        );
    }



    @Test
    public void changedConfirmationIsAttendingTest() {

        // Given
        userRepository.save(user);
        eventRepository.save(event);
        trackRepository.save(track);
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        invitationService.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
                track.getId(), new Confirmation(true)));

        // When
        //invitationService.accept(user.getId(), user.getId(), event.getId(), track.getId());

        // Then
        //assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
        //        track.getId(), new Confirmation(true)));
    }



    //@Test
    //public void modifyTitleTest() {
    //
    //    // Given
    //    userRepository.save(user);
    //    eventRepository.save(event);
    //    trackRepository.save(track);
    //    RoleTitle role = RoleTitle.AUTHOR;
    //    RoleTitle modifiedRole = RoleTitle.ATTENDEE;
    //
    //    // When - Then
    //    Exception e = assertThrows(NoSuchElementException.class, () -> {
    //        service.modifyTitle(user.getId(), user.getId(), event.getId(), track.getId(), modifiedRole);
    //    });
    //    assertEquals(e.getMessage(), "No such attendance is found; cannot be modified.");
    //
    //    // When
    //    service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
    //    service.accept(user.getId(), user.getId(), event.getId(), track.getId());
    //    service.modifyTitle(user.getId(), user.getId(), event.getId(), track.getId(), modifiedRole);
    //
    //    Attendee attendee = service.getAttendance(user.getId(), event.getId(), track.getId());
    //
    //    // Then
    //    assertEquals(attendee.getRole().getRoleTitle(), modifiedRole);
    //}

    //    @Test
    //    public void getAttendanceByEventTest() {
    //
    //
    //
    //        int num = 10;
    //
    //        // When
    //        for (int i = 0; i < num; i++) {
    //            userRepository.save(new AppUser(new Email("test@test.test"), new Name("name"), null, null, null));
    //            invitationService.enroll(i + 1, i + 1, event.getId(), track.getId());
    //        }
    //
    //        // Then
    //        Exception e = assertThrows(NoSuchElementException.class, () -> {
    //                service.getAttendanceByEvent(eventId + 1L);
    //            }
    //        );
    //
    //        assertEquals(e.getMessage(), "No confirmed attendance associated with this event can be found.");
    //
    //
    //        // Then
    //        List<Attendee> retrieved = service.getAttendanceByEvent(eventId);
    //        assertEquals(retrieved.size(), num);
    //
    //        for (int i = 0; i < num; i++) {
    //            assertEquals(retrieved.get(i).getEvent().getId(), eventId);
    //        }
    //    }
    //
    //    @Test
    //    public void getAttendanceByTrackTest() {
    //
    //        // Given
    //        Long userId = 5L;
    //        Long eventId = 10L;
    //        Long trackId = 51L;
    //        RoleTitle role = RoleTitle.AUTHOR;
    //
    //        int num = 10;
    //
    //        // When
    //        for (int i = 0; i < num; i++) {
    //            service.invite(userId + i, userId + i, eventId, trackId, role);
    //            service.accept(userId + i, userId + i, eventId, trackId);
    //        }
    //
    //        // Then
    //        assertThrows(NullPointerException.class, () -> {
    //                service.getAttendanceByTrack(null);
    //            }
    //        );
    //        Exception e = assertThrows(NoSuchElementException.class, () -> {
    //                service.getAttendanceByTrack(trackId + 1L);
    //            }
    //        );
    //
    //        assertEquals(e.getMessage(), "No confirmed attendance associated with this track can be found.");
    //
    //
    //        // Then
    //        List<Attendee> retrieved = service.getAttendanceByTrack(trackId);
    //        assertEquals(retrieved.size(), num);
    //
    //        for (int i = 0; i < num; i++) {
    //            assertEquals(retrieved.get(i).getTrack().getId(), trackId);
    //        }
    //    }

}
