package nl.tudelft.sem.template.domain.attendee;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.domain.HasEvents;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.user.AppUser;

/**
 * An DDD entity that represents an attendee/role in the domain.
 */

@Entity
@Table(name = "attendees")
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Attendee extends HasEvents {

    // Contains the attendance identifier
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "role", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    // Indicates whether the conferred role was accepted.
    @Column(name = "confirmed", nullable = false)
    @Convert(converter = ConfirmationAttributeConverter.class)
    private Confirmation confirmation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = true)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AppUser user;

    public Attendee(Long id) {
        this.id = id;
    }

    /**
     * A constructor.
     */
    public Attendee(Role role, Confirmation confirmation, Event event, Track track, AppUser user) {
        this.role = role;
        this.confirmation = confirmation;
        this.event = event;
        this.track = track;
        this.user = user;
    }

    /**
     * A getter accessor method to bypass the value object {@link Confirmation}
     * and directly extract the confirmation status.
     *
     * @return the confirmation status of the attendance
     */
    public Boolean isConfirmed() {
        return this.confirmation.isConfirmed();
    }

    /**
     * A setter accessor method to bypass the value object {@link Confirmation}
     * and directly change the confirmation status of the entity.
     *
     * @param confirm the new confirmation status of the attendance.
     */
    public void setConfirmation(Boolean confirm) {
        this.confirmation = new Confirmation(confirm);
    }

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
