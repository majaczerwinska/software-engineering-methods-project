package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.Log;

/**
 * The specific ```Log``` abstract class for the ```AppUser``` type.
 */
public abstract class UserLog implements Log {

    transient AppUser subject;

    /**
     * Returns the type of the subject of the log.
     *
     * @return the type of the subject.
     */
    @Override
    public final LogType getLogType() {
        return LogType.USER;
    }
}
