package nl.tudelft.sem.template.example.domain.event;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing an event's name in our domain.
 */
@EqualsAndHashCode
class EventName {
    private final transient String eventNameValue;

    public EventName(String eventName) {
        this.eventNameValue = eventName;
    }

    @Override
    public String toString() {
        return eventNameValue;
    }
}
