package nl.tudelft.sem.template.domain.track;

import lombok.EqualsAndHashCode;
import nl.tudelft.sem.template.domain.event.Event;

/**
 * A DDD value object representing the parent event in our domain.
 */
@EqualsAndHashCode
public class ParentEvent {
    private final transient Event eventValue;

    public ParentEvent(Event event) {
        // Validate input
        this.eventValue = event;
    }

    public Event toEvent() {
        return eventValue;
    }
}