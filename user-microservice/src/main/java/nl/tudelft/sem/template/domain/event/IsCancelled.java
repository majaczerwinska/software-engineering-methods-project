package nl.tudelft.sem.template.domain.event;

/**
 * A DDD value object representing whether an event is cancelled in our domain.
 */
public class IsCancelled {
    private final transient boolean status;

    public IsCancelled(int isCancelled) {
        this.status = isCancelled == 1;
    }

    public int getCancelStatus() {
        return status ? 1 : 0;
    }
}
