package nl.tudelft.sem.template.domain.event;

/**
 * A DDD value object representing an event's description in our domain.
 */

public class EventDescription {

    private final transient String text;

    public EventDescription(String description) {
        this.text = description;
    }

    @Override
    public String toString() {
        return text;
    }
}
