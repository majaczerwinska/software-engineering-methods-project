package nl.tudelft.sem.template.logs.attendee;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * An ```AttendeeLog``` representing the creation of an attendee.
 */
public class CreatedAttendeeLog extends AttendeeLog {

    /**
     * Creates a new AttendeeLog.
     *
     * @param subject the subject of the log.
     */
    public CreatedAttendeeLog(Attendee subject) {
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
        sb.append("Attendee ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully created!\n");
        sb.append(logDate.toString());
        return sb.toString();
    }

}
