package nl.tudelft.sem.template.logs.event;

import java.time.LocalDate;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```EventLog``` representing the moditication of the start date of an Event.
 */
public class StartDateChangedEventLog extends EventLog {

    final transient LocalDate startDate;

    /**
     * Creates a new EventLog.
     *
     * @param subject the subject of the log.
     */
    public StartDateChangedEventLog(Event subject) {
        this.subject = subject;
        this.startDate = subject.getStartDate();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The start date for Event ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(startDate);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
