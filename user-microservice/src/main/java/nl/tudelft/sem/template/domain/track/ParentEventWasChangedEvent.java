package nl.tudelft.sem.template.domain.track;

/**
 * A DDD domain event indicating parent event had changed.
 */
public class ParentEventWasChangedEvent {
    private final Track track;

    public ParentEventWasChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}