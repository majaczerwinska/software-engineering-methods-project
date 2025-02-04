package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```TrackLog``` representing the modification of the description of the Track.
 */
public class DescriptionChangedTrackLog extends TrackLog {

    final transient String description;

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public DescriptionChangedTrackLog(Track subject) {
        this.subject = subject;
        this.description = subject.getDescription().toString();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The description for Track ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to \"");
        sb.append(description);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
