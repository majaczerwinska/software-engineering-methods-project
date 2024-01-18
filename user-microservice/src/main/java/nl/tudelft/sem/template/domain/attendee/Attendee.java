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
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.LogFactory;
import nl.tudelft.sem.template.logs.attendee.AttendeeLog;
import nl.tudelft.sem.template.logs.attendee.AttendeeLogFactory;

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
    @Column(name = "confirmation", nullable = false)
    @Convert(converter = ConfirmationAttributeConverter.class)
    private Confirmation confirmation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
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
        LogFactory.loadFactory(LogType.ATTENDEE).registerCreation(this);
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
        ((AttendeeLogFactory) LogFactory.loadFactory(LogType.ATTENDEE)).registerConfirmationChange(this);
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

    /**
     * Extends the accessor to a public visibility.
     *
     * @param object The log to be recorded.
     */
    public void recordLog(Object object) {
        this.recordThat(object);
    }


    /**
     * A converter from the Domain representation to the API Model representation.
     *
     * @return An API model Attendee instance.
     */
    public nl.tudelft.sem.template.model.Attendee toModel() {
        nl.tudelft.sem.template.model.Attendee model = new nl.tudelft.sem.template.model.Attendee();
        model.setId(this.id);
        model.setUserId(this.getUser().getId());
        model.setEventId(this.getEvent().getId());
        if (this.track != null) {
            model.setTrackId(this.track.getId());
        }
        model.setRole(nl.tudelft.sem.template.model.Role.valueOf(this.role.getRoleTitle().name()));

        return model;
    }

    /**
     * A converter from the Domain representation to the API Model representation.
     *
     * @return An API model Invitation instance.
     */
    public nl.tudelft.sem.template.model.Invitation toInvitationModel() {
        nl.tudelft.sem.template.model.Invitation model = new nl.tudelft.sem.template.model.Invitation();
        model.setId(this.id);
        model.setUserId(this.getUser().getId());
        model.setEventId(this.getEvent().getId());
        if (this.track != null) {
            model.setTrackId(this.track.getId());
        }
        model.setRole(nl.tudelft.sem.template.model.Role.valueOf(this.role.getRoleTitle().name()));

        return model;
    }
}
