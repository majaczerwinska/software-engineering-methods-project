package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.enums.LogKind;

public class UserDeletedEventLog extends UserLog {

    /**
     * Creates a new UserLog.
     *
     * @param subject the subject of the log.
     */
    public UserDeletedEventLog(AppUser subject) {
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
        sb.append("User with id: ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully removed.\n");
        sb.append(".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
