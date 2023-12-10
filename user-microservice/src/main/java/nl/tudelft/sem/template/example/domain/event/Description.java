package nl.tudelft.sem.template.example.domain.event;

/**
 * A DDD value object representing an event's description in our domain.
 */

public class Description {

    private final transient String text;

    public Description(String description) {
        this.text = description;
    }

    @Override
    public String toString() {
        return text;
    }
}
