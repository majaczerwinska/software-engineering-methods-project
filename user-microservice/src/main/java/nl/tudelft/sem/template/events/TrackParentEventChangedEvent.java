package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.track.Track;

/**
 * A DDD domain event indicating parent event had changed.
 */
public class TrackParentEventChangedEvent {
    private final Track track;

    public TrackParentEventChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}