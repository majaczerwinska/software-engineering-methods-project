package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.enums.LogKind;
import nl.tudelft.sem.template.model.PaperType;

/**
 * A ```TrackLog``` representing the modification of the paper requirement of a Track.
 */
public class PaperRequirementChangedTrackLog extends TrackLog {

    final transient PaperType paperType;

    /**
     * Creates a new LogTrack.
     *
     * @param subject the subject of the log.
     */
    public PaperRequirementChangedTrackLog(Track subject) {
        this.subject = subject;
        this.paperType = subject.getPaperType().toPaperType();
        subject.recordLog(this);
    }

    @Override
    public LogKind getLogKind() {
        return LogKind.MODIFICATION;
    }

    @Override
    public String getLogSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("The paper type requirement for Track ");
        sb.append(this.subject.getId());
        sb.append(" has been successfully updated to ");
        sb.append(paperType.name());
        sb.append(".\n");
        sb.append(logDate.toString());
        return sb.toString();
    }
}
