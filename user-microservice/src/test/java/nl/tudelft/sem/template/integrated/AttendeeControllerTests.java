package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.controllers.AttendeeController;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.domain.track.Description;
import nl.tudelft.sem.template.domain.track.PaperRequirement;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.model.PaperType;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;





@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AttendeeControllerTests {

    @MockBean
    private AuthManager authManager;

    @Autowired
    private transient TrackRepository trackRepository;

    @Autowired
    private transient EventRepository eventRepository;

    @Autowired
    private transient AttendeeRepository attendeeRepository;

    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    @InjectMocks
    private transient AttendeeController attendeeController;

    static nl.tudelft.sem.template.domain.event.Event event;
    static nl.tudelft.sem.template.domain.user.AppUser user;
    static nl.tudelft.sem.template.domain.track.Track track;
    static nl.tudelft.sem.template.domain.attendee.Attendee attendee;

    /**
     * Setups the tests.
     */
    @BeforeAll
    public static void setup() {
        LocalDate startDate = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate = LocalDate.parse("2025-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate date0 = LocalDate.parse("2024-04-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate date1 = LocalDate.parse("2024-12-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        event = new nl.tudelft.sem.template.domain.event.Event(
                startDate, endDate, new IsCancelled(false), new EventName("name"), new EventDescription("desc"));
        user = new AppUser(new Email("test@test.net"), new Name("name"));
        track = new Track(new Title("title"), new Description("desc"), new PaperRequirement(PaperType.FULL_PAPER),
                date0, date1, event);


    }


    @BeforeEach
    public void setupBeforeEach() {
        when(authManager.getEmail()).thenReturn("test@test.net");
    }


    @Test
    public void unauthorizedAcessTest() {
        var response1 = attendeeController.createAttendee(attendee.toModel());
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
        var response2 = attendeeController.deleteAttendee(1L);
        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode());
        var response3 = attendeeController.getAttendeeByID(1L);
        assertEquals(HttpStatus.UNAUTHORIZED, response3.getStatusCode());
        var response4 = attendeeController.getFilteredAttendees(null, null, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response4.getStatusCode());
        var response5 = attendeeController.updateAttendee(attendee.toModel());
        assertEquals(HttpStatus.UNAUTHORIZED, response5.getStatusCode());
    }
    
    @Test
    public void getAttendeeByIdTest() {
        user = userRepository.save(user);
        
        // Null identifier
        var response1 = attendeeController.getAttendeeByID(null);
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        // Not found
        var response2 = attendeeController.getAttendeeByID(1L);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());

        event = eventRepository.save(event);
        track = trackRepository.save(track);

        attendee = new Attendee(new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true),
                event, track, user);

        attendee = attendeeRepository.save(attendee);

        // Good Response
        var response3 = attendeeController.getAttendeeByID(attendee.getId());
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertEquals(attendee.toModel(), response3.getBody());

    }


    @Test
    public void deleteAttendeeTest() {
        user = userRepository.save(user);

        // Null identifier
        var response1 = attendeeController.deleteAttendee(null);
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        // Not found
        var response2 = attendeeController.deleteAttendee(1L);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());

        event = eventRepository.save(event);
        track = trackRepository.save(track);

        attendee = new Attendee(new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true),
                event, track, user);

        attendee = attendeeRepository.save(attendee);

        // Good Response
        var response3 = attendeeController.deleteAttendee(attendee.getId());
        assertEquals(HttpStatus.OK, response3.getStatusCode());
    }

    @Test
    public void updateAttendeeTest() {
        user = userRepository.save(user);
        // Null identifier & Attributes
        var response1 = attendeeController.updateAttendee(null);
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        var model = new nl.tudelft.sem.template.model.Attendee();
        var response2 = attendeeController.updateAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        model.setId(1L);
        var response3 = attendeeController.updateAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
        model.setRole(nl.tudelft.sem.template.model.Role.ATTENDEE);
        var response5 = attendeeController.updateAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response5.getStatusCode());

        // Normal usage
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        attendee = new Attendee(new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true),
                event, track, user);

        attendee = attendeeRepository.save(attendee);
        model.setId(attendee.getId());
        model.setRole(nl.tudelft.sem.template.model.Role.ATTENDEE);
        var response4 = attendeeController.updateAttendee(model);
        assertEquals(HttpStatus.OK, response4.getStatusCode());
        assertEquals(response4.getBody().getRole(), nl.tudelft.sem.template.model.Role.ATTENDEE);

    }

    @Test
    public void getFilteredAttendeesTest() {
        user = userRepository.save(user);
        // Null arguments
        var response1 = attendeeController.getFilteredAttendees(null, null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        // Not found
        var response2 = attendeeController.getFilteredAttendees(1L, null, null);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());

        // Null roles
        event = eventRepository.save(event);
        track = trackRepository.save(track);
        attendee = new Attendee(new Role(RoleTitle.PC_CHAIR),
                new Confirmation(true),
                event, track, user);

        attendee = attendeeRepository.save(attendee);

        var response3 = attendeeController.getFilteredAttendees(event.getId(), null, track.getId());
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertEquals(response3.getBody().size(), 1);
        assertEquals(response3.getBody().get(0).getId(), attendee.getId());

        // Empty Roles
        var response4 = attendeeController
                .getFilteredAttendees(event.getId(),
                        new ArrayList<nl.tudelft.sem.template.model.Role>(), track.getId());
        assertEquals(HttpStatus.OK, response4.getStatusCode());
        assertEquals(response3.getBody().size(), 1);
        assertEquals(response3.getBody().get(0).getId(), attendee.getId());

        // Incompatible Role
        var rolesList1 = new ArrayList<nl.tudelft.sem.template.model.Role>();
        rolesList1.add(nl.tudelft.sem.template.model.Role.AUTHOR);
        var response5 = attendeeController.getFilteredAttendees(event.getId(), rolesList1, track.getId());
        assertEquals(HttpStatus.NOT_FOUND, response5.getStatusCode());

        // Compatible Roles
        var rolesList2 = new ArrayList<nl.tudelft.sem.template.model.Role>();
        rolesList2.add(nl.tudelft.sem.template.model.Role.PC_CHAIR);
        var response6 = attendeeController.getFilteredAttendees(event.getId(), rolesList2, track.getId());
        assertEquals(HttpStatus.OK, response6.getStatusCode());
        assertEquals(response3.getBody().size(), 1);
        assertEquals(response3.getBody().get(0).getId(), attendee.getId());

    }

    @Test
    @Transactional
    public void createAttendeeTest() {
        user = userRepository.save(user);
        nl.tudelft.sem.template.model.Attendee model = new nl.tudelft.sem.template.model.Attendee();

        // Null arguments
        var response1 = attendeeController.createAttendee(null);
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        var response2 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        model.setUserId(1L);

        var response3 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());
        model.setEventId(2L);

        var response4 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode());
        model.setRole(nl.tudelft.sem.template.model.Role.PC_CHAIR);

        var response5 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response5.getStatusCode());
        model.setUserId(user.getId());

        var response6 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response6.getStatusCode());
        event = eventRepository.save(event);
        model.setEventId(event.getId());
        model.setTrackId(12301321L);

        var response7 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.BAD_REQUEST, response7.getStatusCode());

        model.setTrackId(null);

        var response8 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.OK, response8.getStatusCode());
        assertTrue(attendeeRepository.existsById(response8.getBody().getId()));

        track = trackRepository.save(track);
        model.setTrackId(track.getId());
        var response9 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.OK, response9.getStatusCode());
        assertTrue(attendeeRepository.existsById(response9.getBody().getId()));

        model.setTrackId(null);
        var response10 = attendeeController.createAttendee(model);
        assertEquals(HttpStatus.CONFLICT, response10.getStatusCode());
    }
}
