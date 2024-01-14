package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EventTests {

    static Event eventEquals0;
    static Event eventEquals1;
    static Event eventNullDesc;

    /**
     * Setups the variables for the tests.
     */
    @BeforeAll
    public static void setup() {
        Long id = 123L;
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        eventEquals0 = new Event(
                id, startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        eventEquals1 = new Event(
                id, startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        eventNullDesc = new Event(
                124L, startDate, endDate, new IsCancelled(false), new EventName("name"), null);
    }

    @Test
    void allArgsConstorTests() {
        assertNotNull(eventNullDesc);
    }

    @Test
    void equalsTests() {
        assertEquals(eventEquals0, eventEquals0);
        assertNotEquals(eventEquals0, null);
        assertNotEquals(eventEquals0, 1231231L);
        assertNotEquals(eventEquals0, eventNullDesc);
        assertEquals(eventEquals0, eventEquals1);
    }

    @Test
    void hashCodeTests() {
        assertEquals(eventNullDesc.hashCode(), Objects.hash(eventNullDesc.getId()));
    }

    @Test
    void eventDescriptionEqualsTests() {
        EventDescription eventDescription0 = new EventDescription("DESC");
        EventDescription eventDescription0Copy = new EventDescription("DESC");
        EventDescription eventDescription1 = new EventDescription("desc");
        assertEquals(eventDescription0, eventDescription0);
        assertEquals(eventDescription0, eventDescription0Copy);
        assertNotEquals(eventDescription0, eventDescription1);
        assertNotEquals(eventDescription0, null);
        assertNotEquals(eventDescription0, "DESC");
    }

    @Test
    void IsCancelledEqualsTests() {
        IsCancelled isCancelledTrue = new IsCancelled(true);
        IsCancelled isCancelledTrueCopy = new IsCancelled(true);
        IsCancelled isCancelledFalse = new IsCancelled(false);

        assertEquals(isCancelledTrue, isCancelledTrue);
        assertEquals(isCancelledTrue, isCancelledTrueCopy);
        assertNotEquals(isCancelledTrue, isCancelledFalse);
        assertNotEquals(isCancelledTrue, null);
        assertNotEquals(isCancelledTrue, true);
    }
}
