package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```UserLog``` representing the modification of the first name of a User.
 */
public class FirstNameChangedUserLog extends UserLog {
    final transient Name firstName;

    /**
     * Creates a new UserLog.
     *
     * @param subject subject of the log
     */
    public FirstNameChangedUserLog(AppUser subject) {
        this.subject = subject;
        this.firstName = subject.getFirstName();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The first name of User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(firstName);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
