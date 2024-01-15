package nl.tudelft.sem.template.logs.event;

import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```EventLog``` representing the creation of a track.
 */
public class CreatedEventLog extends EventLog {

    /**
     * Creates a new EventLog.
     *
     * @param subject the subject of the log.
     */
    public CreatedEventLog(Event subject) {
        this.subject = subject;
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.CREATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully created!\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
