package nl.tudelft.sem.template.example.domain.event;

/**A DDD value object representing whether an event is cancelled in our domain*/
public class IsCancelled {
    private final boolean isCancelled;

    public IsCancelled(int isCancelled) {
        this.isCancelled = isCancelled == 1;
    }

    public int getCancelStatus() {
        return isCancelled ? 1 : 0;
    }
}
