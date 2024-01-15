package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```TrackLog``` representing the deletion of a Track.
 */
public class RemovedTrackLog extends TrackLog {


    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public RemovedTrackLog(Track subject) {
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
        sb.append("Track ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully removed.\n");
        sb.append(".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
