package nl.tudelft.sem.template.example.domain.attendee;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * An DDD entity that represents an attendee/role in the domain.
 */


@Entity
@IdClass(AttendeeId.class)
@Table(name = "attendees")
@NoArgsConstructor
@AllArgsConstructor
public class Attendee {

    // Contains the identifier for the user
    @Id
    @Column(name = "userId", nullable = false)
    private Long userId;

    // Contains the identifier of the associated event.
    @Id
    @Column(name = "eventId", nullable = false)
    private Long eventId;

    // Contains the identifier of the associated track, if any.
    @Id
    @Column(name = "trackId", nullable = true)
    private Long trackId;

    @Getter
    @Setter
    @Column(name = "role", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    // Indicates whether the conferred role was accepted.
    @Getter
    @Setter
    @Column(name = "confirmed", nullable = false)
    @Convert(converter = ConfirmationAttributeConverter.class)
    private Confirmation confirmation;


    /**
     * Two entities are equal if their uID, tID, and eID are equivalent.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Attendee that = (Attendee) o;
        return (Objects.equals(userId, that.userId))
                && (Objects.equals(eventId, that.eventId))
                && (Objects.equals(trackId, that.trackId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId, trackId);
    }
}
