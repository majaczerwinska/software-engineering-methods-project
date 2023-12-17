package nl.tudelft.sem.template.example.domain.attendee;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;


/**
 * The Identifier class for the composite primary keys in the Attendee Class.
 */
@Getter
public class AttendeeId implements Serializable {

    // UID is necessary for a serializable; the value must be unique, and I chose one arbitrarily.
    @Serial
    private static final long serialVersionUID = 1743L;

    private final Long userId;
    private final Long eventId;
    private final Long trackId;

    /**
     * Creates a new instance of the Attendee Identifier class.
     *
     * @param userId the identifier of the associated user
     * @param eventId the identifier of the associated event
     * @param trackId the identifier of the associated track
     */
    public AttendeeId(Long userId, Long eventId, Long trackId) {
        this.userId = userId;
        this.eventId = eventId;
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttendeeId that = (AttendeeId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(eventId, that.eventId)
                && Objects.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId, trackId);
    }
}
