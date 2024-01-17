package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```UserLog``` representing the creation of a user.
 */
public class CreatedUserLog extends UserLog {

    /**
     * Created a new UserLog.
     *
     * @param subject the subject of the log
     */
    public CreatedUserLog(AppUser subject) {
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
        sb.append("User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully created!\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
