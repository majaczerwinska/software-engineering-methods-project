package nl.tudelft.sem.template.domain.track;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;


/**
 * A DDD value object representing a deadline in our domain.
 */
@EqualsAndHashCode
public class Deadline {
    private final transient LocalDate deadlineValue;

    public Deadline(LocalDate deadlineValue) {
        // Validate input
        this.deadlineValue = deadlineValue;
    }

    public LocalDate toDate() {
        return deadlineValue;
    }
}
