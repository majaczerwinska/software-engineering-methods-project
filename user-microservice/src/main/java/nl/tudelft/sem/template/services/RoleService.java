package nl.tudelft.sem.template.services;

import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.attendee.AttendeeRepository;
import nl.tudelft.sem.template.domain.attendee.Confirmation;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service("RoleService")
public class RoleService {
    private final transient UserRepository userRepository;
    private final transient AttendeeRepository attendeeRepository;

    @Autowired
    public RoleService(UserRepository userRepository, AttendeeRepository attendeeRepository) {
        this.userRepository = userRepository;
        this.attendeeRepository = attendeeRepository;
    }

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
    public boolean hasPermission(AuthManager authManager,
                                 Long eventId, @Nullable Long trackId, int level) {
        AppUser user = userRepository.findByEmail(new Email(authManager.getEmail())).orElse(null);
        if (user == null) {
            return false;
        }
        Attendee role = attendeeRepository.findByUserIdAndEventIdAndTrackIdAndConfirmation(user.getId(), eventId, trackId, new Confirmation(true)).orElse(null);
        if (role == null) {
            return false;
        }
        if (!(role.getRole().getRoleTitle().getPermission() <= level)) {
            return false;
        }
        return true;
    }
}
