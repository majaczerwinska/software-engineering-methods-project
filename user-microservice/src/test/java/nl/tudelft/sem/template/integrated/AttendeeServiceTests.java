package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.enums.RoleTitle;
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
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AttendeeServiceTests {

    @Autowired
    private transient AttendeeRepository attendeeRepository;

    @Autowired
    private transient AttendeeService service;

    static AppUser user;
    static Track track;
    static Event event;

        @BeforeAll
    public static void setup() {
        user = new AppUser(1L);
        track = new Track(1L);
        event = new Event(1L);
        }


    @Test
    public void createAttendeeTest() {
        Long trackId = null;

        // Given
        // Self-enroll for the first time
        // When
        service.enroll(user.getId(), event.getId(), null, RoleTitle.ATTENDEE);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), trackId));

        // Self-enroll for the second time throws exception on duplicate creation
        // When & Then
        var e = assertThrows(IllegalArgumentException.class, () -> {
                service.enroll(user.getId(), event.getId(), null, RoleTitle.ATTENDEE);
            }
        );

        assertEquals(e.getMessage(), "Attendance instance already exists.");
    }

    // @Test
    // public void findAttendeeTest() {
    //     // Given
    //     RoleTitle role = RoleTitle.AUTHOR;

    //     // When
    //     service.invite(user, user.getId(), event.getId(), track.getId(), role);
    //     service.accept(user.getId(), user.getId(), event.getId(), track.getId());
    //     Attendee attendee = service.getAttendance(user.getId(), event.getId(), track.getId());

    //     // Then
    //     assertEquals(attendee.getUser(), user);
    //     assertEquals(attendee.getEvent(), event);
    //     assertEquals(attendee.getTrack(), track);
    //     assertTrue(attendee.getConfirmation().isConfirmed());
    //     assertEquals(attendee.getRole().getRoleTitle(), role);
    // }

    // @Test
    // public void findAttendeeNullTrackTest() {
    //     // Given
    //     Long user.getId() = 5L;
    //     Long event.getId() = 10L;
    //     Long track.getId() = null;
    //     RoleTitle role = RoleTitle.ATTENDEE;

    //     // When
    //     service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
    //     service.accept(user.getId(), user.getId(), event.getId(), track.getId());
    //     Attendee attendee = service.getAttendance(user.getId(), event.getId(), track.getId());

    //     // Then
    //     assertEquals(attendee.getUserId(), user.getId());
    //     assertEquals(attendee.getEventId(), event.getId());
    //     assertNull(attendee.getTrackId());
    //     assertTrue(attendee.getConfirmation().isConfirmed());
    //     assertEquals(attendee.getRole().getRoleTitle(), role);
    // }

    @Test
    public void nonExistentRetrievalGetAttendanceTest() {

        // When-Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.getAttendance(1L, 1L, 1L);
            }
        );
        assertEquals(e.getMessage(), "No such attendance can be found.");
    }

    @Test
    public void unconfirmedRetrievalGetAttendanceTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendance(user.getId(), event.getId(), track.getId());
            }
        );
        assertEquals(e.getMessage(), "No confirmed attendance can be found.");
    }

    @Test
    public void unconfirmedIsInvitedTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), track.getId()));
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId() + 1L, event.getId(), track.getId()));
    }

    @Test
    public void nullTrackIdIsInvitedTest() {

        // Given
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), track.getId()));
    }

    @Test
    public void changedConfirmationIsAttendingTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(), track.getId(), true));

        // When
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(), track.getId(), true));
    }

    @Test
    public void nullTrackIdIsAttendingTest() {

        // Given
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(), track.getId(), true));

        // When
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());

        // Then
        assertTrue(attendeeRepository.existsByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), event.getId(), track.getId(), true));
    }

    @Test
    public void getAttendanceByUserTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;

        int num = 10;

        // When
        for (int i = 0; i < num; i++) {
            service.invite(user.getId(), user.getId(), event.getId(), track.getId() + i, role);
            service.accept(user.getId(), user.getId(), event.getId(), track.getId() + i);
        }

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendanceByUser(user.getId() + 1L);
            }
        );

        assertEquals(e.getMessage(), "No confirmed attendance associated with this user can be found.");


        // Then
        List<Attendee> retrieved = service.getAttendanceByUser(user.getId());
        assertEquals(retrieved.size(), num);

        for (int i = 0; i < num; i++) {
            assertEquals(retrieved.get(i).getUser(), user);
        }
    }

    @Test
    public void getAttendanceByEventTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;

        int num = 10;

        // When
        for (int i = 0; i < num; i++) {
            service.invite(user.getId() + i, user.getId() + i, event.getId(), track.getId(), role);
            service.accept(user.getId() + i, user.getId() + i, event.getId(), track.getId());
        }

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendanceByEvent(event.getId() + 1L);
            }
        );

        assertEquals(e.getMessage(), "No confirmed attendance associated with this event can be found.");


        // Then
        List<Attendee> retrieved = service.getAttendanceByEvent(event.getId());
        assertEquals(retrieved.size(), num);

        for (int i = 0; i < num; i++) {
            assertEquals(retrieved.get(i).getEvent(), event);
        }
    }

    @Test
    public void getAttendanceByTrackTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;

        int num = 10;

        // When
        for (int i = 0; i < num; i++) {
            service.invite(user.getId() + i, user.getId() + i, event.getId(), track.getId(), role);
            service.accept(user.getId() + i, user.getId() + i, event.getId(), track.getId());
        }

        // Then
        assertThrows(NullPointerException.class, () -> {
                service.getAttendanceByTrack(null);
            }
        );
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendanceByTrack(track.getId() + 1L);
            }
        );

        assertEquals(e.getMessage(), "No confirmed attendance associated with this track can be found.");


        // Then
        List<Attendee> retrieved = service.getAttendanceByTrack(track.getId());
        assertEquals(retrieved.size(), num);

        for (int i = 0; i < num; i++) {
            assertEquals(retrieved.get(i).getTrack(), track);
        }
    }

    @Test
    public void modifyTitleTest() {

        // Given
        RoleTitle role = RoleTitle.AUTHOR;
        RoleTitle modifiedRole = RoleTitle.ATTENDEE;

        // When - Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.modifyTitle(user.getId(), user.getId(), event.getId(), track.getId(), modifiedRole);
            }
        );
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
        RoleTitle role = RoleTitle.AUTHOR;

        // When - Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.resign(user.getId(), event.getId(), track.getId());
            }
        );
        assertEquals(e.getMessage(), "No such attendance can be found, so no deletion is possible.");

        // When
        service.invite(user.getId(), user.getId(), event.getId(), track.getId(), role);
        service.accept(user.getId(), user.getId(), event.getId(), track.getId());
        service.resign(user.getId(), event.getId(), track.getId());

        // Then
        assertFalse(attendeeRepository.existsByUserIdAndEventIdAndTrackId(user.getId(), event.getId(), track.getId()));
    }
}
