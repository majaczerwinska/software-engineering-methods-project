package nl.tudelft.sem.template.domain.track;

/**
 * A DDD domain event indicating a title had changed.
 */
public class DescriptionWasChangedEvent {
    private final Track track;

    public DescriptionWasChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}