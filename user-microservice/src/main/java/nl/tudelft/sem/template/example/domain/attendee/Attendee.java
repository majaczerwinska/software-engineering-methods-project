package nl.tudelft.sem.template.example.domain.attendee;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;




/**
 * An DDD entity that represents an attendee/role in the domain.
 */


@Entity
//@IdClass(AttendeeId.class)
@Table(name = "attendees")
@NoArgsConstructor
public class Attendee {

    // Contains the identifier for the user
    @Id
    @Column(name = "userId", nullable = false)
    private long userId;

    //Contains the identifier of the associated event.
    @Id
    @Column(name = "eventId", nullable = false)
    private long eventId;


    //Contains the identifier of the associated track, if any.
    @Id
    @Column(name = "trackId", nullable = true)
    private long trackId;

    @Getter
    @Column(name = "role", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    @Getter
    @Column(name = "confirmed", nullable = false)
    @Convert(converter = ConfirmationAttributeConverter.class)
    private Confirmation confirmed;


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
        return (userId == that.userId)
                && (eventId == that.eventId)
                && (trackId == that.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId, trackId);
    }
}
