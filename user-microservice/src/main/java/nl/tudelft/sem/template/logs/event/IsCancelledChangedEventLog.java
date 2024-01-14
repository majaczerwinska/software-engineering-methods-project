package nl.tudelft.sem.template.logs.event;

import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```TrackLog``` representing the moditication of the start date of an Event.
 */
public class IsCancelledChangedEventLog extends EventLog {

    final transient IsCancelled isCancelled;

    /**
     * Creates a new EventLog.
     *
     * @param subject the subject of the log.
     */
    public IsCancelledChangedEventLog(Event subject) {
        this.subject = subject;
        this.isCancelled = subject.getIsCancelled();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The cancelled attribute for Event ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(isCancelled.getCancelStatus());
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
