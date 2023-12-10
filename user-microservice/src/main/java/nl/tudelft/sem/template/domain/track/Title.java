package nl.tudelft.sem.template.domain.track;

import lombok.EqualsAndHashCode;

/**
 * A DDD value object representing a title in our domain.
 */
@EqualsAndHashCode
public class Title {
    private final transient String titleValue;

    public Title(String title) {
        // validate NetID
        this.titleValue = title;
    }

    @Override
    public String toString() {
        return titleValue;
    }
}