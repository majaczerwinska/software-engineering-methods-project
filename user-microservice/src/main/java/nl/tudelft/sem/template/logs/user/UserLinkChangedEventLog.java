package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Link;
import nl.tudelft.sem.template.enums.LogKind;

public class UserLinkChangedEventLog extends UserLog {
    final transient Link link;

    /**
     * Creates a new UserLog.
     *
     * @param subject the subject of the log.
     */
    public UserLinkChangedEventLog(AppUser subject) {
        this.subject = subject;
        this.link = subject.getLink();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The personal website (link) of the User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(link);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
