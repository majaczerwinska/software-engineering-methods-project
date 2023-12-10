package nl.tudelft.sem.template.example.domain.event;

/**
 * A DDD value object representing an event's description in our domain.
 */

public class Description {

    private final String description;

    public Description(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
