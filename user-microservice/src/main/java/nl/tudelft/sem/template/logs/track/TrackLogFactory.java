package nl.tudelft.sem.template.logs.track;

import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.logs.LogFactory;

/**
 * The factory implementation for the Track ```LogFactory```.
 */
public class TrackLogFactory extends LogFactory {

    /**
     * Creates a Log representing the creation of a new track.
     *
     * @param subject the newly created track.
     * @return a Log representing the creation of the track.
     */
    public TrackLog registerCreation(Track subject) {
        return new CreatedTrackLog(subject);
    }

    /**
     * Creates a Log representing the modification of the description of a track.
     *
     * @param subject the modified track.
     * @return a log representing the modification of the track.
     */
    public TrackLog registerDescriptionChange(Track subject) {
        return new DescriptionChangedTrackLog(subject);
    }

    /**
     * Creates a Log representing the modification of the paper requirement of a track.
     *
     * @param subject the modified track.
     * @return a log representing the modification of the track.
     */
    public TrackLog registerPaperRequirementChange(Track subject) {
        return new PaperRequirementChangedTrackLog(subject);
    }

    /**
     * Creates a Log representing the modification of the submission deadline of a track.
     *
     * @param subject the modified track.
     * @return a log representing the modification of the track.
     */
    public TrackLog registerSubmitDeadlineChange(Track subject) {
        return new SubmitDeadlineChangedTrackLog(subject);
    }

    /**
     * Creates a Log representing the modification of the title of a track.
     *
     * @param subject the modified track.
     * @return a log representing the modification of the track.
     */
    public TrackLog registerTitleChange(Track subject) {
        return new TitleChangedTrackLog(subject);
    }

    /**
     * Creates a Log representing the deletion of the track.
     *
     * @param subject the to-be-deleted track.
     * @return a log representing the deletion of the track.
     */
    public TrackLog registerRemoval(Track subject) {
        return new RemovedTrackLog(subject);
    }
}
