package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.enums.LogKind;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.event.CreatedEventLog;
import nl.tudelft.sem.template.logs.event.EndDateChangedEventLog;
import nl.tudelft.sem.template.logs.event.EventDescriptionChangedEventLog;
import nl.tudelft.sem.template.logs.event.EventNameChangedEventLog;
import nl.tudelft.sem.template.logs.event.IsCancelledChangedEventLog;
import nl.tudelft.sem.template.logs.event.StartDateChangedEventLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTests {

    Event eventEquals0;
    Event eventEquals1;
    Event eventNullDesc;

    /**
     * Setups the variables for the tests.
     */
    @BeforeEach
    public void setup() {
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
    void idArgConstructorTest() {
        Event event = new Event(3L);
        assertNotNull(event);
        assertEquals(3, event.getId());
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
    void eventDescriptionHashCodeTest() {
        EventDescription eventDescription = new EventDescription("DESC");
        assertEquals(eventDescription.hashCode(), Objects.hash("DESC"));
    }

    @Test
    void isCancelledEqualsTests() {
        IsCancelled isCancelledTrue = new IsCancelled(true);
        IsCancelled isCancelledTrueCopy = new IsCancelled(true);
        IsCancelled isCancelledFalse = new IsCancelled(false);

        assertEquals(isCancelledTrue, isCancelledTrue);
        assertEquals(isCancelledTrue, isCancelledTrueCopy);
        assertNotEquals(isCancelledTrue, isCancelledFalse);
        assertNotEquals(isCancelledTrue, null);
        assertNotEquals(isCancelledTrue, true);
    }

    @Test
    void isCancelledHashCodeTest() {
        IsCancelled isCancelled = new IsCancelled(true);
        assertEquals(isCancelled.hashCode(), Objects.hash(true));
    }

    @Test
    void createEventLogTest() {
        assertFalse(eventEquals0.getDomainEvents().isEmpty());
        Object domainEvent = eventEquals0.getDomainEvents().get(eventEquals0.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof CreatedEventLog);
        CreatedEventLog eventLog = (CreatedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith("Event 123 has been successfully created!\n"));
        assertEquals(eventLog.getLogType(), LogType.EVENT);
        assertEquals(eventLog.getLogKind(), LogKind.CREATION);
        assertEquals(eventLog.getSubject(), eventEquals0);
    }

    @Test
    void setStartDateTest() {
        eventEquals0.setStartDate(LocalDate.parse("2024-01-29T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME));
        assertFalse(eventEquals0.getDomainEvents().isEmpty());
        Object domainEvent = eventEquals0.getDomainEvents().get(eventEquals0.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof StartDateChangedEventLog);
        StartDateChangedEventLog eventLog = (StartDateChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The start date for Event 123 has been successfully updated to \"2024-01-29\"."));
        assertEquals(eventLog.getLogType(), LogType.EVENT);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), eventEquals0);
    }

    @Test
    void setEndDateTest() {
        eventEquals0.setEndDate(LocalDate.parse("2024-01-29T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME));
        assertFalse(eventEquals0.getDomainEvents().isEmpty());
        Object domainEvent = eventEquals0.getDomainEvents().get(eventEquals0.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof EndDateChangedEventLog);
        EndDateChangedEventLog eventLog = (EndDateChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The end date for Event 123 has been successfully updated to \"2024-01-29\"."));
        assertEquals(eventLog.getLogType(), LogType.EVENT);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), eventEquals0);
    }

    @Test
    void setIsCancelledTest() {
        eventEquals0.setIsCancelled(new IsCancelled(true));
        assertFalse(eventEquals0.getDomainEvents().isEmpty());
        Object domainEvent = eventEquals0.getDomainEvents().get(eventEquals0.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof IsCancelledChangedEventLog);
        IsCancelledChangedEventLog eventLog = (IsCancelledChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The cancelled attribute for Event 123 has been successfully updated to \"true\"."));
        assertEquals(eventLog.getLogType(), LogType.EVENT);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), eventEquals0);
    }

    @Test
    void setEventNameTest() {
        eventEquals0.setName(new EventName("NAME"));
        assertFalse(eventEquals0.getDomainEvents().isEmpty());
        Object domainEvent = eventEquals0.getDomainEvents().get(eventEquals0.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof EventNameChangedEventLog);
        EventNameChangedEventLog eventLog = (EventNameChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The name for Event 123 has been successfully updated to \"NAME\"."));
        assertEquals(eventLog.getLogType(), LogType.EVENT);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), eventEquals0);
    }

    @Test
    void setEventDescriptionTest() {
        eventEquals0.setEventDescription(new EventDescription("DESCRIPTION"));
        assertFalse(eventEquals0.getDomainEvents().isEmpty());
        Object domainEvent = eventEquals0.getDomainEvents().get(eventEquals0.getDomainEvents().size() - 1);
        assertTrue(domainEvent instanceof EventDescriptionChangedEventLog);
        EventDescriptionChangedEventLog eventLog = (EventDescriptionChangedEventLog) domainEvent;
        assertTrue(eventLog.getLogSummary().startsWith(
                "The description for Event 123 has been successfully updated to \"DESCRIPTION\"."));
        assertEquals(eventLog.getLogType(), LogType.EVENT);
        assertEquals(eventLog.getLogKind(), LogKind.MODIFICATION);
        assertEquals(eventLog.getSubject(), eventEquals0);
    }
}
