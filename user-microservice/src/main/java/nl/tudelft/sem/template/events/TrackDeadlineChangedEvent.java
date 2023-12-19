package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.track.Track;

/**
 * A DDD domain event indicating a deadline had changed.
 */
public class TrackDeadlineChangedEvent {
    private final Track track;

    public TrackDeadlineChangedEvent(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return this.track;
    }
}