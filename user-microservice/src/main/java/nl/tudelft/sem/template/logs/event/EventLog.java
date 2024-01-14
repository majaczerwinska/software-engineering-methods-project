package nl.tudelft.sem.template.logs.event;

import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.Log;

/**
 * The specific ```Log``` abstract class for the ```Event``` type.
 */
public abstract class EventLog implements Log<Event> {

    private transient Event subject;

    @Override
    public final LogType getLogType() {
        return LogType.EVENT;
    }

    @Override
    public final Event getSubject() {
        return this.subject;
    }
}
