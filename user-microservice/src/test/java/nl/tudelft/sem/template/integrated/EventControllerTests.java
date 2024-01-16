package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.controllers.EventController;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.Event;
import nl.tudelft.sem.template.services.InvitationService;
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
    @Autowired
    private transient InvitationService invitationService;

    static nl.tudelft.sem.template.domain.event.Event event;
    static AppUser appUser;

    @BeforeAll
    private static void setupStatic() {
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        event = new nl.tudelft.sem.template.domain.event.Event(
                startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        appUser = new AppUser(new Email("test@test.net"), new Name("name"), new Name("name"), null, null, null);
    }

    @BeforeEach
    private void setup() {
        when(authManager.getEmail()).thenReturn("test@test.net");
    }

    @Test
    public void createEventNoUserTest() {
        ResponseEntity<Event> response = eventController.addEvent(event.toModelEvent());
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createEventValidTest() {
        userRepository.save(appUser);
        ResponseEntity<Event> response = eventController.addEvent(event.toModelEvent());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Event responseEvent = response.getBody();
        assertEquals(event.getStartDate(), responseEvent.getStartDate());
        assertEquals(event.getEndDate(), responseEvent.getEndDate());
        assertEquals(event.getIsCancelled().getCancelStatus(), responseEvent.getIsCancelled());
        assertEquals(event.getName().toString(), responseEvent.getName());
        assertEquals(event.getDescription().toString(), responseEvent.getDescription());

        Attendee attendee = invitationService.getAttendee(appUser.getId(),
                responseEvent.getId(),
                null,
                true);
        assertEquals(attendee.getRole().getRoleTitle(), RoleTitle.GENERAL_CHAIR);
    }

    @Test
    public void createEventInvalidTest() {
        userRepository.save(appUser);
        event.setStartDate(null);
        ResponseEntity<Event> response = eventController.addEvent(event.toModelEvent());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findEventTest() {
        userRepository.save(appUser);
        Event responseEvent = eventController.addEvent(event.toModelEvent()).getBody();
        ResponseEntity<List<Event>> response = eventController.findEvent(null, null, null, null, null, null);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<Event> responseEvents = response.getBody();
        assertTrue(responseEvents.contains(responseEvent));
    }

    @Test
    public void updateEventTest() {
        userRepository.save(appUser);
        Event responseEvent = eventController.addEvent(event.toModelEvent()).getBody();
        event.setId(responseEvent.getId());
        event.setIsCancelled(new IsCancelled(true));
        ResponseEntity<Event> response = eventController.updateEvent(event.toModelEvent());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        responseEvent = response.getBody();
        assertEquals(responseEvent, event.toModelEvent());
    }

    @Test
    public void updateEventUnauthorizedTest() {
        userRepository.save(appUser);
        userRepository.save(new AppUser(new Email("test@test.test.test.test"), new Name("NAM12423E"),
                new Name("nam1241e")));

        Event responseEvent = eventController.addEvent(event.toModelEvent()).getBody();
        when(authManager.getEmail()).thenReturn("test@test.test.test.test");
        ResponseEntity<Event> response = eventController.updateEvent(event.toModelEvent());
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }
}
