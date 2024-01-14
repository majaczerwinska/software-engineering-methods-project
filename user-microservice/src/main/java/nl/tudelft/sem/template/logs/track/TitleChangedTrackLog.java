package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogKind;

/**
 * A ```TrackLog``` representing the modification of the Title of a Track.
 */
public class TitleChangedTrackLog extends TrackLog {

    final transient String title;

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public TitleChangedTrackLog(Track subject) {
        this.subject = subject;
        this.title = subject.getTitle().toString();
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
        sb.append(title);
        sb.append("\".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
