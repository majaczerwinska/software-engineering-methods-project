package nl.tudelft.sem.template.domain.track;

/**
 * A DDD domain event indicating a paper requirement had changed.
 */
public class PaperRequirementWasChangedEvent {
    private final Track track;

    public PaperRequirementWasChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}