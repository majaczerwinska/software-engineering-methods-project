package nl.tudelft.sem.template.domain.event;

import java.util.Objects;

/**
 * A DDD value object representing whether an event is cancelled in our domain.
 */
public class IsCancelled {
    private final transient Boolean status;

    public IsCancelled(Boolean isCancelled) {
        this.status = isCancelled;
    }

    public Boolean getCancelStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IsCancelled isCancelled = (IsCancelled) o;
        return Objects.equals(status, isCancelled.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
