package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Communication;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.enums.LogKind;
import org.h2.command.Command;

public class UserCommunicationChangedEventLog extends UserLog {
    final transient Communication communication;

    /**
     * Creates a new UserLog.
     *
     * @param subject the subject of the log.
     */
    public UserCommunicationChangedEventLog(AppUser subject) {
        this.subject = subject;
        this.communication = subject.getCommunication();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The communication of the AppUser ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(communication);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
