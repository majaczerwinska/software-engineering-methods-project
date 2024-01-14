package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```TrackLog``` representing the creation of a track.
 */
public class CreatedTrackLog extends TrackLog {

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public CreatedTrackLog(Track subject) {
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
        sb.append("Track ");
        sb.append(this.subject.getId());
        //sb.append(" in Event "); sb.append(this.subject.getEventId());
        sb.append(" has been successfully created!\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
