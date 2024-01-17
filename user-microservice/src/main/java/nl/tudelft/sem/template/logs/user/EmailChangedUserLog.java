package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```UserLog``` representing the modification of the email address of a User.
 */
public class EmailChangedUserLog extends UserLog {

    final transient Email email;

    /**
     * Creates a new UserLog.
     *
     * @param subject subject of the log
     */
    public EmailChangedUserLog(AppUser subject) {
        this.subject = subject;
        this.email = subject.getEmail();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The email address of User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(email);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
