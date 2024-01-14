package nl.tudelft.sem.template.domain.event;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing an event's name in our domain.
 */
@EqualsAndHashCode
public class EventName {
    private final transient String eventNameValue;

    public EventName(String eventName) {
        this.eventNameValue = eventName;
    }

    @Override
    public String toString() {
        return eventNameValue;
    }
}
