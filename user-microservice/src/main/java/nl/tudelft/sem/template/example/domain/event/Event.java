package nl.tudelft.sem.template.example.domain.event;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.example.domain.HasEvents;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

/**
 * A DDD entity representing an event in our domain.
 */
@Entity
@Table(name = "events")
@NoArgsConstructor
public class Event extends HasEvents {
    /**
     * Identifier for the application event.
     */
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Column(name = "start_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    @Getter
    @Column(name = "end_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    // TODO: Add converter here and change type
    @Getter
    @Column(name = "is_cancelled", nullable = false)
    private boolean isCancelled;

    @Getter
    @Column(name = "name", nullable = false)
    @Convert(converter = EventNameAttributeConverter.class)
    private EventName name;

    // TODO: Add converter here and change type
    @Getter
    @Column(name = "description", nullable = false)
    private String description;

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
