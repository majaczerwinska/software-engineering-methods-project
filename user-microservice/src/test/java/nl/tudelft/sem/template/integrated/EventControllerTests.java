package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.controllers.EventController;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.model.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventControllerTests {

    @MockBean
    private AuthManager authManager;

    @Autowired
    private transient EventRepository eventRepository;

    @Autowired
    private transient AttendeeRepository attendeeRepository;

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    @InjectMocks
    private transient EventController eventController;

    static Event event;
    static AppUser appUser;

    @BeforeAll
    private static void setupStatic() {
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        event = new nl.tudelft.sem.template.domain.event.Event(
                startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"))
                .toModelEvent();
        appUser = new AppUser(new Email("test@test.net"), new Name("name"));
    }

    @BeforeEach
    private void setup() {
        when(authManager.getEmail()).thenReturn("test@test.net");
    }

    @Test
    public void createEventNoUserTest() {
        ResponseEntity<Event> response = eventController.addEvent(event);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createEventValidTest() {
        userRepository.save(appUser);
        ResponseEntity<Event> response = eventController.addEvent(event);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Event responseEvent = response.getBody();
        assertEquals(event.getStartDate(), responseEvent.getStartDate());
        assertEquals(event.getEndDate(), responseEvent.getEndDate());
        assertEquals(event.getIsCancelled(), responseEvent.getIsCancelled());
        assertEquals(event.getName(), responseEvent.getName());
        assertEquals(event.getDescription(), responseEvent.getDescription());
    }
}
