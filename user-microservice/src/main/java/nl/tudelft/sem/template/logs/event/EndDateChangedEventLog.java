package nl.tudelft.sem.template.logs.event;

import java.time.LocalDate;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```EventLog``` representing the moditication of the end date of an Event.
 */
public class EndDateChangedEventLog extends EventLog {

    final transient LocalDate endDate;

    /**
     * Creates a new EventLog.
     *
     * @param subject the subject of the log.
     */
    public EndDateChangedEventLog(Event subject) {
        this.subject = subject;
        this.endDate = subject.getEndDate();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The end date for Event ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(endDate);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
