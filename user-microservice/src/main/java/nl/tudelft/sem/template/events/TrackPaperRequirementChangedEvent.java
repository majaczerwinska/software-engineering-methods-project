package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.track.Track;

/**
 * A DDD domain event indicating a paper requirement had changed.
 */
public class TrackPaperRequirementChangedEvent {
    private final Track track;

    public TrackPaperRequirementChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}