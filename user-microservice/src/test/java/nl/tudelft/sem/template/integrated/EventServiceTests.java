package nl.tudelft.sem.template.integrated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.domain.event.*;
import nl.tudelft.sem.template.services.EventService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain
// beans.
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventServiceTests {

    @Autowired
    private transient EventRepository eventRepository;

    @Autowired
    private transient EventService service;

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private IsCancelled isCancelled;
    private EventName name;
    private EventDescription description;
    private Event savedEvent;

    @BeforeEach
    public void setup() {
        id = 1L;
        startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        isCancelled = new IsCancelled(false);
        name = new EventName("test");
        description = new EventDescription("test");
        savedEvent = new Event(id, startDate, endDate, isCancelled, name, description);
        eventRepository.save(savedEvent);
    }

    @Test
    public void createEventTest() {

        // Given
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        // Create Event for the first time
        // When
        Event returnedEvent = service.createEvent(startDate, endDate, false, "name", "desc");

        // Then
        assertTrue(eventRepository.existsById(returnedEvent.getId()));
    }

    @Test
    public void eventExistsByIdTest() {
        boolean exists = service.eventExistsById(id);

        assertTrue(exists);
    }

    @Test
    public void invalidEventExistsByIdTest() {
        Long nonExistingId = Long.MAX_VALUE;
        boolean exists = service.eventExistsById(nonExistingId);

        assertFalse(exists);
    }

    @Test
    public void getEventByIdTest() {
        Event retrieved = service.getEventById(id);

        assertNotNull(retrieved);
        assertEquals(savedEvent, retrieved);
    }

    @Test
    public void getInvalidEventByIdTest() {
        Long nonExistingId = Long.MAX_VALUE; // Assume this ID does not exist
        Event event = service.getEventById(nonExistingId);

        assertNull(event);
    }

    @Test
    public void deleteValidEventTest() {
        boolean deleted = service.deleteEvent(savedEvent.getId());

        assertTrue(deleted);

        Event searchResult = service.getEventById(id);
        assertNull(searchResult);
    }

    @Test
    public void deleteInvalidEventTest() {
        Long nonExistingId = Long.MAX_VALUE;
        boolean deleted = service.deleteEvent(nonExistingId);

        assertFalse(deleted);
    }

}
