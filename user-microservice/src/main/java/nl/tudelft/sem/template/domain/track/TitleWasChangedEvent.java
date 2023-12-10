package nl.tudelft.sem.template.domain.track;

/**
 * A DDD domain event indicating a title had changed.
 */
public class TitleWasChangedEvent {
    private final Track track;

    public TitleWasChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}
