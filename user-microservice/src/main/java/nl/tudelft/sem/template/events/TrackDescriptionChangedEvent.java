package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.track.Track;

/**
 * A DDD domain event indicating a description had changed.
 */
public class TrackDescriptionChangedEvent {
    private final Track track;

    public TrackDescriptionChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}