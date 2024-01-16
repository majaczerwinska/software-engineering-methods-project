package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import nl.tudelft.sem.template.Application;
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
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.InvitationService;
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
    static AppUser user2;
    static Track track;
    static Track track2;
    static Event event;

    /**
     * Initialize globals.
     */
    @BeforeEach
    public void setup() {
        LocalDate date0 = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate date1 = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        user = new AppUser(new Email("test@test.test"), new Name("name"), new Name("name"), null, null, null);
        user = new AppUser(new Email("test@test.test"), new Name("name"), new Name("name"), null, null, null);
        user2 = new AppUser(new Email("test2@test.test"), new Name("name3"), null, null, null);
        event = new Event(date0, date1, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        track = new Track(new Title("title"), new Description("desc"), new PaperRequirement(PaperType.FULL_PAPER),
                date0, date1, event);

        track2 = new Track(new Title("title132"), new Description("desc"), new PaperRequirement(PaperType.FULL_PAPER),
                date0, date1, event);
    }

    @Test
    public void createAttendanceTest() {

        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);

        // Expected usage
        RoleTitle roleTitle = RoleTitle.ATTENDEE;
        var attendee = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        assertEquals(attendee.getUser(), user);
        assertEquals(attendee.getTrack(), track);
        assertEquals(attendee.getEvent(), event);
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), roleTitle);

        // Creating the same instance twice.
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        });
        assertEquals("Attendance instance already exists.", e.getMessage());

        // Expected usage
        var roleTitle2 = RoleTitle.GENERAL_CHAIR;
        attendee = attendeeService.createAttendance(user.getId(), event.getId(), null, roleTitle2, true);
        assertEquals(attendee.getUser(), user);
        assertEquals(attendee.getEvent(), event);
        assertNull(attendee.getTrack());
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), roleTitle2);
    }

    @Test
    public void deleteAttendanceTest() {
        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle roleTitle = RoleTitle.ATTENDEE;

        // Deleting an inexistent attendance
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            attendeeService.deleteAttendance(1L);
        });
        assertEquals("No such attendance can be found, so no deletion is possible.", e.getMessage());

        // Expected usage
        var attendee = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        assertDoesNotThrow(() -> attendeeService.deleteAttendance(attendee.getId()));
        assertTrue(eventRepository.existsById(event.getId()));
        assertTrue(trackRepository.existsById(track.getId()));

        // Persistent Deletion
        var attendee2 = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        eventRepository.deleteById(event.getId());
        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.deleteAttendance(attendee2.getId());
        });

        assertFalse(eventRepository.existsById(event.getId()));
    }

    @Test
    public void getAttendanceTest() {
        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle roleTitle = RoleTitle.ATTENDEE;

        // Inexistent attendance
        assertNull(attendeeService.getAttendance(1L));

        // Normal usage
        var attendee = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        assertEquals(attendee, attendeeService.getAttendance(attendee.getId()));
    }

    @Test
    public void existsTest() {
        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle roleTitle = RoleTitle.ATTENDEE;

        // Inexistent attendance
        assertFalse(attendeeService.exists(1L));

        // Normal usage
        var attendee = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        assertTrue(attendeeService.exists(attendee.getId()));
    }

    @Test
    public void getFilteredAttendance() {

        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle roleTitle = RoleTitle.ATTENDEE;

        var attendee = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);
        // Normal usage
        var list = attendeeService.getFilteredAttendance(user.getId(), event.getId(), track.getId(), true);
        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), attendee);

        // Inexistent instances
        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.getFilteredAttendance(100L, 100L, 100L, true);
        });

        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.getFilteredAttendance(null, 100L, 100L, true);
        });

        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.getFilteredAttendance(null, null, 100L, true);
        });



        // Null confirmation
        var list2 = attendeeService.getFilteredAttendance(user.getId(), event.getId(), track.getId(), null);
        assertNotNull(list2);
        assertEquals(list2.size(), 1);
        assertEquals(list2.get(0), attendee);

        // Empty list
        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.getFilteredAttendance(user.getId(), event.getId(), track.getId(), false);
        });
    }

    @Test
    public void modifyTitleTest() {
        // Given
        user = userRepository.save(user);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle roleTitle = RoleTitle.ATTENDEE;
        var attendee = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle, true);

        // Inexistent attendance
        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.modifyTitle(1212L, RoleTitle.ATTENDEE);
        });

        // Normal usage
        var value = attendeeService.modifyTitle(attendee.getId(), RoleTitle.AUTHOR);
        assertEquals(value.getRole().getRoleTitle(), RoleTitle.AUTHOR);

        // Null identifier
        assertThrows(NoSuchElementException.class, () -> {
            attendeeService.modifyTitle(null, RoleTitle.ATTENDEE);
        });
    }

    @Test
    public void sufficesTest() {

        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        RoleTitle roleTitle1 = RoleTitle.ATTENDEE;


        // Inexistent subject
        assertThrows(IllegalArgumentException.class, () -> {
            attendeeService.suffices(user2.getId(), 31531L);
        });

        // Inexistent executor
        var attendee1 = attendeeService.createAttendance(user.getId(), event.getId(), null, roleTitle1, true);
        assertFalse(attendeeService.suffices(user2.getId(), attendee1.getId()));

        // Executor with a different track
        attendeeService.createAttendance(user2.getId(), event.getId(), track.getId(), roleTitle1, true);
        assertFalse(attendeeService.suffices(user2.getId(), attendee1.getId()));

        // Normal usage
        RoleTitle roleTitle2 = RoleTitle.GENERAL_CHAIR;
        var attendee2 = attendeeService.createAttendance(user2.getId(), event.getId(), null, roleTitle2, true);
        assertTrue(attendeeService.suffices(user2.getId(), attendee2.getId()));




    }

    @Test
    public void sufficesTest2() {

        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        track2 = trackRepository.save(track2);
        RoleTitle roleTitle1 = RoleTitle.ATTENDEE;
        RoleTitle roleTitle2 = RoleTitle.GENERAL_CHAIR;

        var attendee1 = attendeeService.createAttendance(user.getId(), event.getId(), track.getId(), roleTitle1, true);

        attendeeService.createAttendance(user2.getId(), event.getId(), track2.getId(), roleTitle1, true);
        assertFalse(attendeeService.suffices(user2.getId(), attendee1.getId()));
        attendeeService.createAttendance(user2.getId(), event.getId(), track.getId(), roleTitle2, true);
        assertTrue(attendeeService.suffices(user2.getId(), attendee1.getId()));
        var attendee2 = attendeeService.createAttendance(user.getId(), event.getId(), track2.getId(), roleTitle2, true);
        assertFalse(attendeeService.suffices(user2.getId(), attendee2.getId()));



    }

    //    @Test
    //    @Transactional
    //    public void findAttendeeTest() {
    //        // Given
    //        user = userRepository.save(user);
    //        event = eventRepository.save(event);
    //        track = trackRepository.save(track);
    //        RoleTitle role = RoleTitle.AUTHOR;
    //
    //        // When
    //        invitationService.enroll(user.getId(), event.getId(), track.getId(), role);
    //        Attendee attendee = invitationService.getAttendee(user.getId(), event.getId(),
    //                track.getId(), true);
    //
    //        // Then
    //        assertEquals(attendee.getUser(), user);
    //        assertEquals(attendee.getEvent(), event);
    //        assertEquals(attendee.getTrack(), track);
    //        assertTrue(attendee.getConfirmation().isConfirmed());
    //        assertEquals(attendee.getRole().getRoleTitle(), role);
    //        assertThrows(NoSuchElementException.class, () -> {
    //            invitationService.getAttendee(user.getId(), event.getId(), track.getId(), false);
    //        });
    //    }

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



    //    @Test
    //    public void nonExistentRetrievalGetInvitationTest() {
    //
    //        // When-Then
    //        assertThrows(NoSuchElementException.class, () -> {
    //                invitationService.getInvitations();
    //            }
    //        );
    //    }



    //    @Test
    //    public void changedConfirmationIsAttendingTest() {
    //
    //        // Given
    //        userRepository.save(user);
    //        eventRepository.save(event);
    //        trackRepository.save(track);
    //        RoleTitle role = RoleTitle.AUTHOR;
    //
    //        // When
    //        invitationService.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
    //
    //        // Then
    //        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
    //                track.getId(), new Confirmation(true)));
    //
    //        // When
    //        //invitationService.accept(user.getId(), user.getId(), event.getId(), track.getId());
    //
    //        // Then
    //        //assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(),
    //        //        track.getId(), new Confirmation(true)));
    //    }



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
