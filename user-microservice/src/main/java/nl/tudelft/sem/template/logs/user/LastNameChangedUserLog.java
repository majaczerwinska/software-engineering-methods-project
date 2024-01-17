package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```UserLog``` representing the modification of the last name of a User.
 */
public class LastNameChangedUserLog extends UserLog {
    final transient Name lastName;

    /**
     * Creates a new UserLog.
     *
     * @param subject subject of the log
     */
    public LastNameChangedUserLog(AppUser subject) {
        this.subject = subject;
        this.lastName = subject.getLastName();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The last name of User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(lastName);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
