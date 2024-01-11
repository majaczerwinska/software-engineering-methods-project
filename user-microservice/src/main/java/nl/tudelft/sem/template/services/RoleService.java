package nl.tudelft.sem.template.services;

import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service("RoleService")
public class RoleService {
    /**
     * Checks if the user has permission for a given event and track.
     *
     * @param userService      The service for managing user-related operations.
     * @param authManager      Used for authentication-related checks.
     * @param attendeeService  The service for attendee-related operations.
     * @param eventId          ID of the event
     * @param trackId          ID of the track (nullable if checking event-level permission).
     * @param level            The required permission level. (lower the number, the more permission user have)
     * @return True if the user has the required permission; otherwise, false.
     */
    public boolean hasPermission(UserService userService, AuthManager authManager, AttendeeService attendeeService,
                                 Long eventId, @Nullable Long trackId, int level) {
        AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
        if (user == null) {
            return false;
        }
        Attendee role = attendeeService.getAttendance(user.getId(), eventId, trackId);
        if (! role.isConfirmed()) {
            return false;
        }
        if (!(role.getRole().getRoleTitle().getPermission() <= level)) {
            return false;
        }
        return true;
    }
}

