package nl.tudelft.sem.template.logs.event;

import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```TrackLog``` representing the moditication of the name of an Event.
 */
public class EventNameChangedEventLog extends EventLog {

    final transient EventName eventName;

    /**
     * Creates a new EventLog.
     *
     * @param subject the subject of the log.
     */
    public EventNameChangedEventLog(Event subject) {
        this.subject = subject;
        this.eventName = subject.getName();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The name for Event ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(eventName);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
