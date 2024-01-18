package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Objects;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.ConfirmationAttributeConverter;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.attendee.RoleAttributeConverter;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.enums.LogKind;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.logs.LogFactory;
import nl.tudelft.sem.template.logs.attendee.AttendeeLogFactory;
import nl.tudelft.sem.template.logs.attendee.ConfirmationChangeAttendeeLog;
import nl.tudelft.sem.template.logs.attendee.CreatedAttendeeLog;
import nl.tudelft.sem.template.logs.attendee.RemovedAttendeeLog;
import nl.tudelft.sem.template.logs.attendee.RoleChangedAttendeeLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class AttendeeTests {

    static AppUser user;
    static Track track;
    static Event event;
    static Attendee nullAttendee;
    static Attendee attendeeEquals;
    static Attendee attendeeEquals2;
    static Attendee setterAttendee;

    /**
     * Setups the variables for the tests.
     */
    @BeforeAll
    public static void setup() {
        user = new AppUser();
        track = new Track(2L);
        event = new Event(3L);

        user.setId(1L);


        nullAttendee = new Attendee(
                0L,
                new Role(RoleTitle.ATTENDEE),
                new Confirmation(true),
                null, null, null);

        setterAttendee = new Attendee(
                1L,
                new Role(RoleTitle.ATTENDEE),
                new Confirmation(false),
                event, track, user);

        attendeeEquals = new Attendee(2L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true),
                event, track, user);

        attendeeEquals2 = new Attendee(2L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true),
                event, track, user);
    }

    @Test
    void noArgsConstructorTest() {
        assertNotNull(nullAttendee);
    }

    @Test
    void allArgsConstorTests() {
        assertNotNull(nullAttendee);

        // Check the confirmation
        assertNotNull(nullAttendee.getConfirmation());
        assertEquals(nullAttendee.isConfirmed(), true);

        // Check the role
        assertNotNull(nullAttendee.getRole());
        assertEquals(nullAttendee.getRole().getRoleTitle(), RoleTitle.ATTENDEE);

    }

    @Test
    void setConfirmationTest() {

        Confirmation cnf = setterAttendee.getConfirmation();
        setterAttendee.setConfirmation(true);

        assertNotEquals(cnf, setterAttendee.getConfirmation());
        assertNotEquals(cnf.isConfirmed(), setterAttendee.isConfirmed());
    }

    @Test
    void equalsTests() {
        assertEquals(attendeeEquals, attendeeEquals);
        assertNotEquals(attendeeEquals, null);
        assertNotEquals(attendeeEquals, 1231231L);
        assertNotEquals(attendeeEquals, nullAttendee);
        assertEquals(attendeeEquals, attendeeEquals2);

    }

    @Test
    void hashCodeTests() {

        // Just to cover this base
        assertEquals(nullAttendee.hashCode(), Objects.hash(0L));
    }

    @Test
    void confirmationConverterTest() {
        ConfirmationAttributeConverter conv = new ConfirmationAttributeConverter();

        Confirmation cnf = new Confirmation(true);
        String str = conv.convertToDatabaseColumn(cnf);
        assertEquals(str, "true");
        assertTrue(conv.convertToEntityAttribute(str).isConfirmed());
        assertFalse(conv.convertToEntityAttribute("false").isConfirmed());
        assertNull(conv.convertToDatabaseColumn(null));
    }

    @Test
    void roleConverterTest() {
        RoleAttributeConverter conv = new RoleAttributeConverter();

        Role role = conv.convertToEntityAttribute(RoleTitle.SUB_REVIEWER.name());
        assertEquals(role.getRoleTitle(), RoleTitle.SUB_REVIEWER);
        assertEquals(conv.convertToDatabaseColumn(role), "SUB_REVIEWER");
    }

    @Test
    void toModelTest() {
        var model = setterAttendee.toModel();
        assertNotNull(model);
        assertTrue(model instanceof nl.tudelft.sem.template.model.Attendee);
        assertEquals(model.getId(), setterAttendee.getId());
        assertEquals(model.getUserId(), setterAttendee.getUser().getId());
        assertEquals(model.getEventId(), setterAttendee.getEvent().getId());
        assertEquals(model.getTrackId(), setterAttendee.getTrack().getId());
        assertEquals(model.getRole().name(), setterAttendee.getRole().getRoleTitle().name());
        var attendeeTemp = new Attendee(
            1L,
            new Role(RoleTitle.ATTENDEE),
            new Confirmation(false),
            event, null, user);

        assertNull(attendeeTemp.toModel().getTrackId());
    }

    @Test
    void createdAttendeeLog() {
        // Setup
        Attendee loggableAttendee = new Attendee(1L,
            new Role(RoleTitle.ATTENDEE),
            new Confirmation(true),
            event, track, user);
        AttendeeLogFactory attendeeLogFactory = (AttendeeLogFactory) LogFactory.loadFactory(LogType.ATTENDEE);

        // Log creation

        final var createdLog = attendeeLogFactory.registerCreation(loggableAttendee);
        final var roleLog =  attendeeLogFactory.registerRoleChange(loggableAttendee);
        final var confLog = attendeeLogFactory.registerConfirmationChange(loggableAttendee);
        final var delLog = attendeeLogFactory.registerRemoval(loggableAttendee);


        // Log type verification
        assertTrue(createdLog instanceof CreatedAttendeeLog);
        assertTrue(roleLog instanceof RoleChangedAttendeeLog);
        assertTrue(confLog instanceof ConfirmationChangeAttendeeLog);
        assertTrue(delLog instanceof RemovedAttendeeLog);

        // Log type verification
        assertEquals(createdLog.getLogType(), LogType.ATTENDEE);
        assertEquals(roleLog.getLogType(), LogType.ATTENDEE);
        assertEquals(confLog.getLogType(), LogType.ATTENDEE);
        assertEquals(delLog.getLogType(), LogType.ATTENDEE);

        // Log kind verification
        assertEquals(createdLog.getLogKind(), LogKind.CREATION);
        assertEquals(roleLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(confLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(delLog.getLogKind(), LogKind.REMOVAL);

        // Summary verification
        assertTrue(createdLog.getLogSummary().contains("created"));
        assertTrue(roleLog.getLogSummary().contains("updated"));
        assertTrue(confLog.getLogSummary().contains("updated"));
        assertTrue(delLog.getLogSummary().contains("removed"));


    }

}
