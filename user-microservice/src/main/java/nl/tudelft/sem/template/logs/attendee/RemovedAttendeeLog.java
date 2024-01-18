package nl.tudelft.sem.template.logs.attendee;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * An ```AttendeeLog``` representing the deletion of an Attendee.
 */
public class RemovedAttendeeLog extends AttendeeLog {

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public RemovedAttendeeLog(Attendee subject) {
        this.subject = subject;
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.REMOVAL;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Attendee ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully removed.\n");
        sb.append(".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }

}
