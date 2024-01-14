package nl.tudelft.sem.template.logs.track;

import java.time.LocalDate;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogKind;


/**
 * A ```TrackLog``` representing the modification of the submission date of the Track.
 */
public class SubmitDeadlineChangedTrackLog extends TrackLog {

    final transient LocalDate deadline;

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public SubmitDeadlineChangedTrackLog(Track subject) {
        this.subject = subject;
        this.deadline = subject.getSubmitDeadline();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The submission deadline for Track ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to ");
        sb.append(deadline.toString());
        sb.append(".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
