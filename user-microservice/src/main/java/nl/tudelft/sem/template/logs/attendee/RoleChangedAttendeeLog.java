package nl.tudelft.sem.template.logs.attendee;

import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.enums.LogKind;
import nl.tudelft.sem.template.enums.RoleTitle;

/**
 * An ```AttendeeLog``` representing the modification of the attendance role of an Attendee.
 */
public class RoleChangedAttendeeLog extends AttendeeLog {

    final transient RoleTitle roleTitle;

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public RoleChangedAttendeeLog(Attendee subject) {
        this.subject = subject;
        this.roleTitle = subject.getRole().getRoleTitle();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The attendance role for Attendee ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(roleTitle.name());
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }

}
