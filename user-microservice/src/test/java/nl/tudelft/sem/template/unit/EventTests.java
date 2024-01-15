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
    void allArgsConstructorTests() {
        assertNotNull(eventNullDesc);
    }

    @Test
    void notAllArgsConstructorTest() {
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        IsCancelled ic = new IsCancelled(false);
        EventName name = new EventName("test");
        EventDescription desc = new EventDescription("test");
        assertNotNull(new Event(startDate, endDate, ic, name, desc));
    }

    @Test
    void testNoArgsConstructor() {
        Event event = new Event();
        assertNotNull(event);
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
    void toModelTest() {
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        IsCancelled ic = new IsCancelled(false);
        EventName name = new EventName("test");
        EventDescription desc = new EventDescription("test");
        Event domainEvent = new Event(startDate, endDate, ic, name, desc);
        nl.tudelft.sem.template.model.Event modelEvent = domainEvent.toModelEvent();

        assertEquals(domainEvent.getId(), modelEvent.getId());
        assertEquals(domainEvent.getStartDate(), modelEvent.getStartDate());
        assertEquals(domainEvent.getEndDate(), modelEvent.getEndDate());
        assertEquals(domainEvent.getIsCancelled().getCancelStatus(), modelEvent.getIsCancelled());
        assertEquals(domainEvent.getName().toString(), modelEvent.getName());
        assertEquals(domainEvent.getDescription().toString(), modelEvent.getDescription());
    }
}
