package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.event.Event;

/**
 * A DDD domain event indicating a track had been created.
 */
public class TrackCreatedEvent {
    private final Event event;

    private final Long trackId;

    public TrackCreatedEvent(Event event, Long trackId) {
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
