package nl.tudelft.sem.template.unit;

import nl.tudelft.sem.template.domain.attendee.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AttendeeTests {

    //Test variables
    static Attendee emptyAttendee;
    static Attendee nullAttendee;
    static Attendee attendeeEquals;
    static Attendee attendeeDiffUser;
    static Attendee attendeeDiffEvent;
    static Attendee attendeeEquals2;

    @BeforeAll
    public static void setup(){

        emptyAttendee = new Attendee();

        nullAttendee = new Attendee(
                3L,
                4L,
                null,
                new Role(RoleTitle.ATTENDEE),
                new Confirmation(true)
        );

        attendeeEquals = new Attendee(
                3L,
                4L,
                5L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true)
        );

        attendeeDiffUser = new Attendee(
                15123L,
                4L,
                5L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true)
        );

        attendeeDiffEvent = new Attendee(
                3L,
                12352143L,
                5L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true)
        );

        attendeeEquals2 = new Attendee(
                3L,
                4L,
                5L,
                new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true)
        );
    }

    @Test
    void noArgsConstructorTest(){
        assertNotNull(emptyAttendee);
    }

    @Test
    void allArgsConstorTests(){
        assertNotNull(nullAttendee);

        //Check the confirmation
        assertNotNull(nullAttendee.getConfirmation());
        assertEquals(nullAttendee.getConfirmation().isConfirmed(), true);

        //Check the role
        assertNotNull(nullAttendee.getRole());
        assertEquals(nullAttendee.getRole().getRoleTitle(), RoleTitle.ATTENDEE);

    }


    @Test
    void equalsTests(){

        assertEquals(attendeeEquals,attendeeEquals);
        assertNotEquals(attendeeEquals, null);
        assertNotEquals(attendeeEquals, 1231231L);
        assertNotEquals(attendeeEquals,attendeeDiffUser);
        assertNotEquals(attendeeEquals,attendeeDiffEvent);
        assertNotEquals(attendeeEquals, nullAttendee);
        assertEquals(attendeeEquals,attendeeEquals2);

    }

    @Test
    void hashCodeTests(){

        //Just to cover this base
        assertEquals(nullAttendee.hashCode(), Objects.hash(3L,4L,null));
    }


    @Test
    void confirmationConverterTest(){
        ConfirmationAttributeConverter conv = new ConfirmationAttributeConverter();

        Confirmation cnf = conv.convertToEntityAttribute("Y");
        assertEquals(cnf.isConfirmed(), true);
        assertEquals(conv.convertToDatabaseColumn(cnf), "Y");
    }

    @Test
    void roleConverterTest(){
        RoleAttributeConverter conv = new RoleAttributeConverter();

        Role role = conv.convertToEntityAttribute(RoleTitle.SUB_REVIEWER.name());
        assertEquals(role.getRoleTitle(), RoleTitle.SUB_REVIEWER);
        assertEquals(conv.convertToDatabaseColumn(role), "SUB_REVIEWER");
    }




}
