package nl.tudelft.sem.template.logs.event;

import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```EventLog``` representing the moditication of the name of an Event.
 */
public class EventDescriptionChangedEventLog extends EventLog {

    final transient EventDescription eventDescription;

    /**
     * Creates a new EventLog.
     *
     * @param subject the subject of the log.
     */
    public EventDescriptionChangedEventLog(Event subject) {
        this.subject = subject;
        this.eventDescription = subject.getDescription();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The description for Event ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(eventDescription);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
