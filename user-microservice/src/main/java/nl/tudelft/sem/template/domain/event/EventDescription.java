package nl.tudelft.sem.template.domain.event;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventDescription eventDescription = (EventDescription) o;
        return Objects.equals(text, eventDescription.text);
    }
}
