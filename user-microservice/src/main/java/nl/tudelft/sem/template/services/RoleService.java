package nl.tudelft.sem.template.services;

import java.util.NoSuchElementException;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;


@Service("RoleService")
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class RoleService {
    private final transient UserRepository userRepository;
    private final transient AttendeeService attendeeService;

    @Autowired
    public RoleService(UserRepository userRepository, AttendeeService attendeeService) {
        this.userRepository = userRepository;
        this.attendeeService = attendeeService;
    }

    /**
     * Checks if the user has permission for a given event and track.
     *
     * @param authManager      Used for authentication-related checks.
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
        Attendee role;
        try {
            role = attendeeService.getFilteredAttendance(user.getId(), eventId, trackId, true).get(0);
        } catch (Exception e) {
            return false;
        }
        return role.getRole().getRoleTitle().getPermission() <= level;
    }
}
