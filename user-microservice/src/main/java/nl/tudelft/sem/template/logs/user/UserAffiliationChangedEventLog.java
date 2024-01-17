package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.enums.LogKind;

public class UserAffiliationChangedEventLog extends UserLog {

    final transient UserAffiliation userAffiliation;

    /**
     * Creates a new UserLog.
     *
     * @param subject the subject of the log.
     */
    public UserAffiliationChangedEventLog(AppUser subject) {
        this.subject = subject;
        this.userAffiliation = subject.getAffiliation();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The affiliation of the User ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(userAffiliation);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
