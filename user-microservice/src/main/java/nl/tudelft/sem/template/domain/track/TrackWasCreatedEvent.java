package nl.tudelft.sem.template.domain.track;

import nl.tudelft.sem.template.domain.Event;

/**
 * A DDD domain event indicating a track had been created.
 */
public class TrackWasCreatedEvent {
    private final Event event;

    private final Long trackId;

    public TrackWasCreatedEvent(Event event, Long trackId) {
        this.event = event;
        this.trackId = trackId;
    }

    public Event getEvent() {
        return this.event;
    }

    public Long getTrackId() {
        return this.trackId;
    }
}
