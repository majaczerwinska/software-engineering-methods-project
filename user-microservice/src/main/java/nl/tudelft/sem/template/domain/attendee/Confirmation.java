package nl.tudelft.sem.template.domain.attendee;

import lombok.Getter;

/**
 * A DDD value object representing if an invitation was confirmed or not in our domain.
 */
@Getter
public class Confirmation {

    private final transient boolean confirmed;

    public Confirmation(boolean confirmed) {
        this.confirmed = confirmed;
    }

}
