package nl.tudelft.sem.template.domain.event;

import java.time.LocalDate;
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
import nl.tudelft.sem.template.domain.HasEvents;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

/**
 * A DDD entity representing an event in our domain.
 */
@Entity
@Table(name = "events")
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
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
    @NonNull
    private EventDescription description;

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
        return id == (event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
