package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
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

    @Autowired
    private transient UserRepository userRepository;

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private IsCancelled isCancelled;
    private EventName name;
    private EventDescription description;
    private Event savedEvent;
    static AppUser user;

    /**
     * Creates a new event and saves it to the repository.
     */
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

    /**
     * Initialize globals.
     */
    @BeforeAll
    public static void beforeAllSetup() {
        user = new AppUser(new Email("test@test.test"), new Name("name"), new Name("name"), null, null, null);
    }

    @Test
    public void createEventTest() {
        user = userRepository.save(user);
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        Event returnedEvent = service.createEvent(startDate, endDate, false, "name", "desc", "test@test.test");

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

    @Test
    public void updateEventTest() {
        user = userRepository.save(user);
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        LocalDate startDateNew = LocalDate.parse("2024-01-19T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDateNew = LocalDate.parse("2024-01-11T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        Event createdEvent = service.createEvent(startDate, endDate, false, "name", "desc", "test@test.test");

        Event updatedEvent = service.updateEvent(createdEvent.getId(), startDateNew, endDateNew, true, "NAME", "DESC");
        assertEquals(updatedEvent.getId(), createdEvent.getId());
        assertEquals(updatedEvent.getStartDate(), startDateNew);
        assertEquals(updatedEvent.getEndDate(), endDateNew);
        assertEquals(updatedEvent.getIsCancelled(), new IsCancelled(true));
        assertEquals(updatedEvent.getName(), new EventName("NAME"));
        assertEquals(updatedEvent.getDescription(), new EventDescription("DESC"));
    }
}
