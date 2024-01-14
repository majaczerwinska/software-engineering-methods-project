package nl.tudelft.sem.template.domain.event;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nl.tudelft.sem.template.domain.HasEvents;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.LogFactory;
import nl.tudelft.sem.template.logs.event.EventLogFactory;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

/**
 * A DDD entity representing an event in our domain.
 */
@Entity
@Table(name = "events")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class Event extends HasEvents {
    /**
     * Identifier for the application event.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    @NonNull
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NonNull
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    @Column(name = "is_cancelled", nullable = false)
    @NonNull
    @Convert(converter = IsCancelledAttributeConverter.class)
    private IsCancelled isCancelled;

    @Column(name = "name", nullable = false)
    @NonNull
    @Convert(converter = EventNameAttributeConverter.class)
    private EventName name;

    @Column(name = "description", nullable = false)
    @Convert(converter = EventDescriptionAttributeConverter.class)
    private EventDescription description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> tracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees;

    /**
     * Constructor with nullable description.
     */
    public Event(LocalDate startDate, LocalDate endDate, IsCancelled isCancelled, EventName name,
            EventDescription description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCancelled = isCancelled;
        this.name = name;
        this.description = description;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerCreation(this));
    }

    /**
     * Constructor with nullable description.
     */
    public Event(Long id, LocalDate startDate, LocalDate endDate, IsCancelled isCancelled, EventName name,
            EventDescription description) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCancelled = isCancelled;
        this.name = name;
        this.description = description;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerCreation(this));
    }

    /**
     * Convert self into an API Model Event.
     */
    public nl.tudelft.sem.template.model.Event toModelEvent() {
        nl.tudelft.sem.template.model.Event returnedEvent = new nl.tudelft.sem.template.model.Event();
        returnedEvent.setId(this.getId());
        returnedEvent.setStartDate(this.getStartDate());
        returnedEvent.setEndDate(this.getEndDate());
        returnedEvent.setIsCancelled(this.getIsCancelled().getCancelStatus());
        returnedEvent.setName(this.getName().toString());
        returnedEvent.setDescription(this.getDescription().toString());
        return returnedEvent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerStartDateChange(this));
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerEndDateChange(this));
    }

    public void setIsCancelled(IsCancelled isCancelled) {
        this.isCancelled = isCancelled;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerIsCancelledChange(this));
    }

    public void setName(EventName name) {
        this.name = name;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerEventNameChange(this));
    }

    public void setEventDescription(EventDescription description) {
        this.description = description;
        this.recordThat(((EventLogFactory) LogFactory.loadFactory(LogType.EVENT)).registerEventDescriptionChange(this));
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(id, event.id);
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
}
