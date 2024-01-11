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
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.services.AttendeeService;
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


    @Test
    public void createAttendeeTest() {

        // Given
        Long userId = 1231231L;
        Long eventId = 612412L;

        // Self-enroll for the first time
        // When
        service.enroll(userId, eventId, null, RoleTitle.ATTENDEE);

        // Then
        assertTrue(attendeeRepository.exists(userId, eventId));

        // Self-enroll for the second time throws exception on duplicate creation
        // When & Then
        var e = assertThrows(IllegalArgumentException.class, () -> {
                service.enroll(userId, eventId, null, RoleTitle.ATTENDEE);
            }
        );

        assertEquals(e.getMessage(), "Attendance instance already exists.");
    }

    @Test
    public void findAttendeeTest() {
        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(userId, userId, eventId, trackId, role);
        service.accept(userId, userId, eventId, trackId);
        Attendee attendee = service.getAttendance(userId, eventId, trackId);

        // Then
        assertEquals(attendee.getUserId(), userId);
        assertEquals(attendee.getEventId(), eventId);
        assertEquals(attendee.getTrackId(), trackId);
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), role);
    }

    @Test
    public void findAttendeeNullTrackTest() {
        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = null;
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(userId, userId, eventId, trackId, role);
        service.accept(userId, userId, eventId, trackId);
        Attendee attendee = service.getAttendance(userId, eventId, trackId);

        // Then
        assertEquals(attendee.getUserId(), userId);
        assertEquals(attendee.getEventId(), eventId);
        assertNull(attendee.getTrackId());
        assertTrue(attendee.getConfirmation().isConfirmed());
        assertEquals(attendee.getRole().getRoleTitle(), role);
    }

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
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(userId, userId, eventId, trackId, role);

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendance(userId, eventId, trackId);
            }
        );
        assertEquals(e.getMessage(), "No confirmed attendance can be found.");
    }

    @Test
    public void unconfirmedIsInvitedTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(userId, userId, eventId, trackId, role);

        // Then
        assertTrue(service.isInvited(userId, eventId, trackId));
        assertFalse(service.isInvited(userId + 1L, eventId, trackId));
    }

    @Test
    public void nullTrackIdIsInvitedTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = null;
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(userId, userId, eventId, trackId, role);

        // Then
        assertTrue(service.isInvited(userId, eventId, trackId));
    }

    @Test
    public void changedConfirmationIsAttendingTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        // When
        service.invite(userId, userId, eventId, trackId, role);

        // Then
        assertFalse(service.isAttending(userId, eventId, trackId));

        // When
        service.accept(userId, userId, eventId, trackId);

        // Then
        assertTrue(service.isAttending(userId, eventId, trackId));
    }

    @Test
    public void nullTrackIdIsAttendingTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = null;
        RoleTitle role = RoleTitle.ATTENDEE;

        // When
        service.invite(userId, userId, eventId, trackId, role);

        // Then
        assertFalse(service.isAttending(userId, eventId, trackId));

        // When
        service.accept(userId, userId, eventId, trackId);

        // Then
        assertTrue(service.isAttending(userId, eventId, trackId));
    }

    @Test
    public void getAttendanceByUserTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        int num = 10;

        // When
        for (int i = 0; i < num; i++) {
            service.invite(userId, userId, eventId, trackId + i, role);
            service.accept(userId, userId, eventId, trackId + i);
        }

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendanceByUser(userId + 1L);
            }
        );

        assertEquals(e.getMessage(), "No confirmed attendance associated with this user can be found.");


        // Then
        List<Attendee> retrieved = service.getAttendanceByUser(userId);
        assertEquals(retrieved.size(), num);

        for (int i = 0; i < num; i++) {
            assertEquals(retrieved.get(i).getUserId(), userId);
        }
    }

    @Test
    public void getAttendanceByEventTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        int num = 10;

        // When
        for (int i = 0; i < num; i++) {
            service.invite(userId + i, userId + i, eventId, trackId, role);
            service.accept(userId + i, userId + i, eventId, trackId);
        }

        // Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendanceByEvent(eventId + 1L);
            }
        );

        assertEquals(e.getMessage(), "No confirmed attendance associated with this event can be found.");


        // Then
        List<Attendee> retrieved = service.getAttendanceByEvent(eventId);
        assertEquals(retrieved.size(), num);

        for (int i = 0; i < num; i++) {
            assertEquals(retrieved.get(i).getEventId(), eventId);
        }
    }

    @Test
    public void getAttendanceByTrackTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        int num = 10;

        // When
        for (int i = 0; i < num; i++) {
            service.invite(userId + i, userId + i, eventId, trackId, role);
            service.accept(userId + i, userId + i, eventId, trackId);
        }

        // Then
        assertThrows(NullPointerException.class, () -> {
                service.getAttendanceByTrack(null);
            }
        );
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.getAttendanceByTrack(trackId + 1L);
            }
        );

        assertEquals(e.getMessage(), "No confirmed attendance associated with this track can be found.");


        // Then
        List<Attendee> retrieved = service.getAttendanceByTrack(trackId);
        assertEquals(retrieved.size(), num);

        for (int i = 0; i < num; i++) {
            assertEquals(retrieved.get(i).getTrackId(), trackId);
        }
    }

    @Test
    public void modifyTitleTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;
        RoleTitle modifiedRole = RoleTitle.ATTENDEE;

        // When - Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
            service.modifyTitle(userId, userId, eventId, trackId, modifiedRole);
            }
        );
        assertEquals(e.getMessage(), "No such attendance is found; cannot be modified.");

        // When
        service.invite(userId, userId, eventId, trackId, role);
        service.accept(userId, userId, eventId, trackId);
        service.modifyTitle(userId, userId, eventId, trackId, modifiedRole);

        Attendee attendee = service.getAttendance(userId, eventId, trackId);

        // Then
        assertEquals(attendee.getRole().getRoleTitle(), modifiedRole);
    }

    @Test
    public void deleteAttendanceTest() {

        // Given
        Long userId = 5L;
        Long eventId = 10L;
        Long trackId = 51L;
        RoleTitle role = RoleTitle.AUTHOR;

        // When - Then
        Exception e = assertThrows(NoSuchElementException.class, () -> {
                service.resign(userId, eventId, trackId);
            }
        );
        assertEquals(e.getMessage(), "No such attendance can be found, so no deletion is possible.");

        // When
        service.invite(userId, userId, eventId, trackId, role);
        service.accept(userId, userId, eventId, trackId);
        service.resign(userId, eventId, trackId);

        // Then
        assertFalse(service.isInvited(userId, eventId, trackId));
    }
}
