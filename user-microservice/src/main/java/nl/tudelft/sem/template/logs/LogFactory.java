package nl.tudelft.sem.template.logs;

import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.attendee.AttendeeLogFactory;
import nl.tudelft.sem.template.logs.event.EventLogFactory;
import nl.tudelft.sem.template.logs.track.TrackLogFactory;
import nl.tudelft.sem.template.logs.user.UserLogFactory;

/**
 * Defines the abstract class of a LogFactory.
 */
public abstract class LogFactory {


    /**
     * Returns the corresponding factory based on subject type.
     *
     * @param logType the type of the subject
     * @return the corresponding LogFactory.
     */
    public static LogFactory loadFactory(LogType logType) {
        return switch (logType) {
            case USER -> new UserLogFactory();
            case EVENT -> new EventLogFactory();
            case TRACK -> new TrackLogFactory();
            case ATTENDEE -> new AttendeeLogFactory();
        };
    }

    public abstract Log registerCreation(Object subject);
}
