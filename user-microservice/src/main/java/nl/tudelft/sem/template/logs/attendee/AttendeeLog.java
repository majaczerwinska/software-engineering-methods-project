package nl.tudelft.sem.template.logs.attendee;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.Log;

/**
 * The specific ```Log``` abstract class for the ```Attendee``` type.
 */
public abstract class AttendeeLog implements Log<Attendee> {

    private transient Attendee subject;

    @Override
    public final LogType getLogType() {
        return LogType.ATTENDEE;
    }

    @Override
    public final Attendee getSubject() {
        return this.subject;
    }
}
