package nl.tudelft.sem.template.domain.track;

/**
 * A DDD domain event indicating a deadline had changed.
 */
public class DeadlineWasChangedEvent {
    private final Track track;

    public DeadlineWasChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}