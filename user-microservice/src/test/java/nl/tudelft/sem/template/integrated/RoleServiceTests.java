package nl.tudelft.sem.template.integrated;

import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.attendee.Role;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.enums.RoleTitle;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.RoleService;
import nl.tudelft.sem.template.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    private static AppUser user;
    private static long eventId;
    private static long trackId;
    private static Attendee role;
    private static Email userEmail;
    @Mock
    private UserService userService;
    @Mock
    private AuthManager authManager;
    @Mock
    private AttendeeService attendeeService;
    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        userEmail = new Email("test@test.net");
        user = new AppUser(userEmail, new Name("name"));
        eventId = 33L;
        trackId = 520L;
        role = new Attendee(1L, user.getId(), eventId, trackId,
                new Role(RoleTitle.PC_CHAIR), new Confirmation(true));
        when(authManager.getEmail()).thenReturn(userEmail.toString());
    }

    @Test
    public void hasRightPermissionTest() {
        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(attendeeService.getAttendance(user.getId(), eventId, trackId)).thenReturn(role);

        assertTrue(roleService.hasPermission(userService, authManager, attendeeService, eventId, trackId, 1));
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(attendeeService, times(1)).getAttendance(user.getId(), eventId, trackId);
    }

    @Test
    public void hasBetterPermissionTest() {
        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(attendeeService.getAttendance(user.getId(), eventId, trackId)).thenReturn(role);
        assertTrue(roleService.hasPermission(userService, authManager, attendeeService, eventId, trackId, 4));
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(attendeeService, times(1)).getAttendance(user.getId(), eventId, trackId);
    }

    @Test
    public void hasNoPermissionTest() {
        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(attendeeService.getAttendance(user.getId(), eventId, trackId)).thenReturn(role);
        assertFalse(roleService.hasPermission(userService, authManager, attendeeService, eventId, trackId, 0));
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(attendeeService, times(1)).getAttendance(user.getId(), eventId, trackId);
    }

    @Test
    public void hasNoUserTest() {
        when(userService.getUserByEmail(userEmail)).thenReturn(null);
        assertFalse(roleService.hasPermission(userService, authManager, attendeeService, eventId, trackId, 1));
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).getUserByEmail(userEmail);
    }

    @Test
    public void notConfirmedTest() {
        when(userService.getUserByEmail(userEmail)).thenReturn(user);
        when(attendeeService.getAttendance(user.getId(), eventId, trackId)).thenReturn(role);
        role.setConfirmation(false);
        assertFalse(roleService.hasPermission(userService, authManager, attendeeService, eventId, trackId, 0));
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).getUserByEmail(userEmail);
        verify(attendeeService, times(1)).getAttendance(user.getId(), eventId, trackId);
    }
}
