package nl.tudelft.sem.template.logs.attendee;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * An ```AttendeeLog``` representing the modification of the confirmation status of an Attendee.
 */
public class ConfirmationChangeAttendeeLog extends AttendeeLog {

    final transient boolean confirmed;

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public ConfirmationChangeAttendeeLog(Attendee subject) {
        this.subject = subject;
        this.confirmed = subject.getConfirmation().isConfirmed();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The confirmation status for Attendee ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(confirmed);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }

}
