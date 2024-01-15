package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
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
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.EventService;
import nl.tudelft.sem.template.services.RoleService;
import nl.tudelft.sem.template.services.UserService;
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

    @MockBean
    private transient AttendeeRepository attendeeRepository;

    @Autowired
    private transient UserRepository userRepository;

    @MockBean
    private transient RoleService roleService;

    @MockBean
    private transient EventService eventService;

    @MockBean
    private transient UserService userService;

    @MockBean
    private transient AttendeeService attendeeService;

    @Autowired
    @InjectMocks
    private transient EventController eventController;

    static Event event;
    static AppUser appUser;

    static nl.tudelft.sem.template.domain.event.Event domainEvent;

    @BeforeAll
    private static void setupStatic() {
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        event = new nl.tudelft.sem.template.domain.event.Event(
                startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"))
                .toModelEvent();
        domainEvent = new nl.tudelft.sem.template.domain.event.Event(
                startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        appUser = new AppUser(new Email("test@test.net"), new Name("name"));
    }

    @BeforeEach
    private void setup() {
        when(authManager.getEmail()).thenReturn("test@test.net");
    }

    @Test
    public void createEventNoUserTest() {
        when(userService.getUserByEmail(any(Email.class))).thenReturn(null);
        ResponseEntity<Event> response = eventController.addEvent(event);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createEventValidTest() {
        when(userService.getUserByEmail(new Email("test@test.net"))).thenReturn(appUser);
        when(eventService.createEvent(any(LocalDate.class), any(LocalDate.class),
                any(Boolean.class), any(String.class), any(String.class))).thenReturn(domainEvent);
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

    @Test
    public void getInvalidEventById() {
        when(userService.getUserByEmail(new Email("test@test.net"))).thenReturn(appUser);
        when(eventService.eventExistsById(2115L)).thenReturn(false);
        ResponseEntity<Event> response = eventController.getEventById(2115L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getValidEventById() {
        when(eventService.eventExistsById(anyLong())).thenReturn(true);
        when(userService.getUserByEmail(new Email("test@test.net"))).thenReturn(appUser);

        userRepository.save(appUser);

        nl.tudelft.sem.template.domain.event.Event domainEvent = new nl.tudelft.sem.template.domain.event.Event(
                event.getStartDate(), event.getEndDate(), new IsCancelled(event.getIsCancelled()),
                new EventName(event.getName()), new EventDescription(event.getDescription()));
        eventRepository.save(domainEvent);

        when(eventService.getEventById(anyLong())).thenReturn(domainEvent);
        ResponseEntity<Event> response = eventController.getEventById(domainEvent.getId());
        System.out.println(response.getStatusCode());
        assertEquals(domainEvent.toModelEvent(), response.getBody());
    }

    @Test
    public void getEventByIdUnauthorizedTest() {
        nl.tudelft.sem.template.domain.event.Event domainEvent = new nl.tudelft.sem.template.domain.event.Event(
                event.getStartDate(), event.getEndDate(), new IsCancelled(event.getIsCancelled()),
                new EventName(event.getName()), new EventDescription(event.getDescription()));
        eventRepository.save(domainEvent);
        ResponseEntity<Event> response = eventController.getEventById(domainEvent.getId());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void deleteEventUnauthorizedTest() {
        when(roleService.hasPermission(any(UserService.class), any(AuthManager.class), any(AttendeeService.class),
                anyLong(), any(), anyInt())).thenReturn(false);
        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void deleteValidEventTest() {
        when(roleService.hasPermission(any(UserService.class), any(AuthManager.class),
                any(AttendeeService.class), anyLong(), any(), anyInt())).thenReturn(true);
        when(eventService.deleteEvent(anyLong())).thenReturn(true);

        ResponseEntity<Void> response = eventController.deleteEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService).deleteEvent(anyLong());
    }

    @Test
    void deleteInvalidEventTest() {
        Long eventId = 1L;
        when(roleService.hasPermission(any(UserService.class), any(AuthManager.class),
                any(AttendeeService.class), anyLong(), any(), anyInt())).thenReturn(true);
        when(eventService.deleteEvent(eventId)).thenReturn(false);

        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(eventService).deleteEvent(eventId);
    }
}
