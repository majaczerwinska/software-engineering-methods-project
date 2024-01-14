package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.track.Track;

/**
 * A DDD domain event indicating a title had changed.
 */
public class TrackTitleChangedEvent {
    private final Track track;

    public TrackTitleChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}
