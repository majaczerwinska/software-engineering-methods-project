package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.Log;

/**
 * The specific ```Log``` abstract class for the ```Track``` type.
 */
public abstract class TrackLog implements Log<Track> {
    transient Track subject;

    @Override
    public final LogType getLogType() {
        return LogType.TRACK;
    }

    @Override
    public final Track getSubject() {
        return this.subject;
    }

}
