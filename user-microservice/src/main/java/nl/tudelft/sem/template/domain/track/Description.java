package nl.tudelft.sem.template.domain.track;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing a description in our domain.
 */
@EqualsAndHashCode
public class Description {
    private final transient String descriptionValue;

    public Description(String description) {
        // validate NetID
        this.descriptionValue = description;
    }

    @Override
    public String toString() {
        return descriptionValue;
    }
}