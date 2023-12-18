package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.ConfirmationAttributeConverter;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.attendee.RoleAttributeConverter;
import nl.tudelft.sem.template.enums.RoleTitle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AttendeeTests {


    static Attendee nullAttendee;
    static Attendee attendeeEquals;
    static Attendee attendeeEquals2;

    /**
     * Setups teh variables for the tests.
     */
    @BeforeAll
    public static void setup() {

        nullAttendee = new Attendee(
                1L,
                3L,
                4L,
                null,
                new Role(RoleTitle.ATTENDEE),
                new Confirmation(true)
        );

        attendeeEquals = new Attendee(
                5L,
                3L,
                4L,
                5L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true)
        );

        attendeeEquals2 = new Attendee(
                5L,
                3L,
                4L,
                5L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true)
        );
    }

    @Test
    void noArgsConstructorTest() {
        assertNotNull(nullAttendee);
    }

    @Test
    void allArgsConstorTests() {
        assertNotNull(nullAttendee);

        //Check the confirmation
        assertNotNull(nullAttendee.getConfirmation());
        assertEquals(nullAttendee.getConfirmation().isConfirmed(), true);

        //Check the role
        assertNotNull(nullAttendee.getRole());
        assertEquals(nullAttendee.getRole().getRoleTitle(), RoleTitle.ATTENDEE);

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

        //Just to cover this base
        assertEquals(nullAttendee.hashCode(), Objects.hash(1L));
    }


    @Test
    void confirmationConverterTest() {
        ConfirmationAttributeConverter conv = new ConfirmationAttributeConverter();

        Confirmation cnf = new Confirmation(true);
        String str = conv.convertToDatabaseColumn(cnf);
        assertEquals(str, "true");
        assertTrue(conv.convertToEntityAttribute(str).isConfirmed());
    }

    @Test
    void roleConverterTest() {
        RoleAttributeConverter conv = new RoleAttributeConverter();

        Role role = conv.convertToEntityAttribute(RoleTitle.SUB_REVIEWER.name());
        assertEquals(role.getRoleTitle(), RoleTitle.SUB_REVIEWER);
        assertEquals(conv.convertToDatabaseColumn(role), "SUB_REVIEWER");
    }




}
