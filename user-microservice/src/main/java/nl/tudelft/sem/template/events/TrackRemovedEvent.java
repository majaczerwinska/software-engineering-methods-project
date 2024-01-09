package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.event.Event;

/**
 * A DDD domain event indicating a track had been created.
 */
public class TrackRemovedEvent {
    private final Long parentEventId;
    private final Long trackId;

    public TrackRemovedEvent(Long parentEventId, Long trackId) {
        this.parentEventId = parentEventId;
        this.trackId = trackId;
    }
    public Long getParentEventId() {
        return this.parentEventId;
    }
    public Long getTrackId() {
        return this.trackId;
    }
}
