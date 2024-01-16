package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.services.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RoleServiceTests {

    private static AppUser user;
    private static Long eventId;
    private static Long trackId;
    private static Attendee role;
    private static Email userEmail;

    @MockBean
    private UserRepository userRepository;
    @Mock
    private AuthManager authManager;
    @MockBean
    private AttendeeRepository attendeeRepository;

    @InjectMocks
    @Autowired
    private transient RoleService roleService;

    /**
     * Set up the commonly used objects for role service testing.
     */
    @BeforeEach
    public void setup() {
        userEmail = new Email("test@test.net");
        user = new AppUser(userEmail, new Name("name"), new Name("name"));
        eventId = 33L;
        trackId = 520L;
        role = new Attendee(new Role(RoleTitle.PC_CHAIR), new Confirmation(true),
                new Event(), new Track(), user);
        when(authManager.getEmail()).thenReturn(userEmail.toString());
    }

    @Test
    public void hasRightPermissionTest() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(attendeeRepository.findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
                new Confirmation(true))).thenReturn(Optional.of(role));

        assertTrue(roleService.hasPermission(authManager, eventId, trackId, 1));
        verify(authManager, times(1)).getEmail();
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(attendeeRepository, times(1)).findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
            new Confirmation(true));
    }

    @Test
    public void hasBetterPermissionTest() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(attendeeRepository.findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
                new Confirmation(true))).thenReturn(Optional.of(role));
        assertTrue(roleService.hasPermission(authManager, eventId, trackId, 4));
        verify(authManager, times(1)).getEmail();
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(attendeeRepository, times(1)).findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
            new Confirmation(true));
    }

    @Test
    public void hasNoPermissionTest() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(attendeeRepository.findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
                new Confirmation(true))).thenReturn(Optional.of(role));
        assertFalse(roleService.hasPermission(authManager, eventId, trackId, 0));
        verify(authManager, times(1)).getEmail();
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(attendeeRepository, times(1)).findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
            new Confirmation(true));
    }

    @Test
    public void hasNoUserTest() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        assertFalse(roleService.hasPermission(authManager, eventId, trackId, 1));
        verify(authManager, times(1)).getEmail();
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    public void notConfirmedTest() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(attendeeRepository.findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
                new Confirmation(true))).thenReturn(Optional.of(role));
        role.setConfirmation(false);
        assertFalse(roleService.hasPermission(authManager, eventId, trackId, 0));
        verify(authManager, times(1)).getEmail();
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(attendeeRepository, times(1)).findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId,
            new Confirmation(true));
    }
}
