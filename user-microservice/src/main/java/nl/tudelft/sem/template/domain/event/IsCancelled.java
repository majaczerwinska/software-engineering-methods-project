package nl.tudelft.sem.template.domain.event;

/**
 * A DDD value object representing whether an event is cancelled in our domain.
 */
public class IsCancelled {
    private final transient boolean status;

    public IsCancelled(boolean isCancelled) {
        this.status = isCancelled;
    }

    public boolean getCancelStatus() {
        return status;
    }
}
