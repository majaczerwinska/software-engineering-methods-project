package nl.tudelft.sem.template.logs;

import java.time.LocalDateTime;
import nl.tudelft.sem.template.enums.LogKind;
import nl.tudelft.sem.template.enums.LogType;


/**
 * This interface provides the ```Log``` object to be used in the ```HasLogs```
 * class.
 *
 * @param <T> the type of the subject of the log.
 */
public interface Log {
    LocalDateTime logDate = LocalDateTime.now();

    /**
     * Returns the type of the subject of the log.
     *
     * @return the type of the subject.
     */
    public LogType getLogType();

    /**
     * Returns the kind of operation represented by the log.
     *
     * @return the kind of operation performed.
     */
    public LogKind getLogKind();

    /**
     * A human-readable summary outlining the contents of the log.
     *
     * @return A human-readable summary.
     */
    public String getLogSummary();

    /**
     * Returns the creation time of the ```Log``` object.
     *
     * @return the creation time.
     */
    default LocalDateTime getLogTime() {
        return this.logDate;
    }
}
