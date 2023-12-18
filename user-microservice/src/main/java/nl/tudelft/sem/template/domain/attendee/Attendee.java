package nl.tudelft.sem.template.domain.attendee;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * An DDD entity that represents an attendee/role in the domain.
 */


@Entity
@Table(name = "attendees")
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Attendee {

    // Contains the attendance identifier
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    // Contains the identifier for the user
    @Column(name = "userId", nullable = false)
    @NonNull
    private final Long userId;

    // Contains the identifier of the associated event.
    @Column(name = "eventId", nullable = false)
    @NonNull
    private final Long eventId;

    // Contains the identifier of the associated track, if any.
    @Column(name = "trackId", nullable = true)
    private final Long trackId;

    @Setter
    @Column(name = "role", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    @NonNull
    private Role role;

    // Indicates whether the conferred role was accepted.
    @Setter
    @Column(name = "confirmed", nullable = false)
    @Convert(converter = ConfirmationAttributeConverter.class)
    @NonNull
    private Confirmation confirmation;


    /**
     * Two entities are equal if their identifiers are equivalent.
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
