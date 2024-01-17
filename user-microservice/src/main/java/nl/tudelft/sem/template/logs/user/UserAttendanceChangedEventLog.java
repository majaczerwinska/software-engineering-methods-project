package nl.tudelft.sem.template.logs.user;

import java.util.List;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.enums.LogKind;


public class UserAttendanceChangedEventLog extends UserLog {

    final transient List<Attendee> attendance;

    /**
     * Creates a new UserLog.
     *
     * @param subject the subject of the log.
     */
    public UserAttendanceChangedEventLog(AppUser subject) {
        this.subject = subject;
        this.attendance = subject.getAttendance();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The list of attendees of the User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(attendance);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
