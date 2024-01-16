package nl.tudelft.sem.template.domain.attendee;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Confirmation confirmation = (Confirmation) o;
        return Objects.equals(confirmed, confirmation.confirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmed);
    }
}
