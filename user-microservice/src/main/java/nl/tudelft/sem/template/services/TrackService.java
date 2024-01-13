package nl.tudelft.sem.template.services;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * A DDD service for track.
 */
@Service
public class TrackService {
    private final transient TrackRepository trackRepository;

    /**
     * A constructor dependency injection for the track JPA Repository concrete implementation.
     *
     * @param trackRepository the track repository injection
     */
    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    /**
     * Creates a new track.
     *
     * @param track - the track to be created
     * @return - the created track
     */
    @Transactional
    public Track createTrack(Track track) throws IllegalArgumentException {
        if (track == null) {
            throw new IllegalArgumentException("Null reference for track");
        }
        if (track.getId() < 0) {
            throw new IllegalArgumentException("Invalid track id");
        }
        if (track.getTitle() == null || track.getTitle().toString() == null) {
            throw new IllegalArgumentException("Null reference for track title");
        }
        if (track.getParentEventId() < 0) {
            throw new IllegalArgumentException("Invalid parent event id");
        }
        if (trackRepository.existsById(track.getId())) {
            throw new IllegalArgumentException("Track with id:" + track.getId() + " already exist.");
        }
        return trackRepository.save(track);
    }

    /**
     * Deletes the track with the specified id.
     *
     * @param id - id of the to be deleted track
     * @return - the deleted track
     */
    @Transactional
    public Track deleteTrackById(long id) throws NoSuchElementException {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid track id: " + id);
        }
        Optional<Track> track = trackRepository.findById(id);
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with id:" + id + " does not exist.");
        }
        trackRepository.delete(track.get());
        return track.get();
    }

    /**
     * Updates an existing track account and saves it in the repository.
     *
     * @param track - the updated track account to be saved.
     * @return the updated track that was saved.
     */
    @Transactional
    public Track updateTrack(Track track) throws IllegalArgumentException, NoSuchElementException {
        if (track.getTitle() == null || track.getTitle().toString() == null) {
            throw new IllegalArgumentException("Null reference for track title");
        }
        if (trackRepository.existsByTitleAndParentEventId(track.getTitle(), track.getParentEventId())) {
            throw new IllegalArgumentException("Track with this title already exist in the event.");
        }
        if (!trackRepository.existsById(track.getId())) {
            throw new NoSuchElementException("Track with id:" + track.getId() + " does not exist.");
        }
        return trackRepository.save(track);
    }

    /**
     * Retrieves the track with the specified id.
     *
     * @param id - id of a track
     * @return - track with this id if exists, else null
     */
    public Track getTrackById(long id) throws NoSuchElementException, IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid track ID");
        }
        Optional<Track> track = trackRepository.findById(id);
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with ID: " + id + " does not exist.");
        }

        return track.get();
    }

    /**
     * Retrieves all tracks with the specified title.
     *
     * @param title - title of a track
     * @return - tracks with this title if exists, else null
     */
    public List<Track> getTrackByTitle(Title title) throws NoSuchElementException, IllegalArgumentException {
        if (title == null || title.toString() == null) {
            throw new IllegalArgumentException("Null reference for track title");
        }
        List<Track> tracks = trackRepository.findByTitle(title);
        if (tracks.isEmpty()) {
            throw new NoSuchElementException("Track with title:" + title.toString() + " can not be found.");
        }
        return tracks;
    }

    /**
     * Retrieves all tracks within the specified event.
     *
     * @param parentEventId - parent event of a track
     * @return - tracks within this event if exists, else null
     */
    public List<Track> getTrackByParentEvent(long parentEventId) throws NoSuchElementException, IllegalArgumentException {
        if (parentEventId < 0) {
            throw new IllegalArgumentException("Invalid parent id");
        }
        List<Track> tracks = trackRepository.findByParentEventId(parentEventId);
        if (tracks.isEmpty()) {
            throw new NoSuchElementException("No Track exist in event:" + parentEventId);
        }
        return tracks;
    }

    /**
     * Retrieves track with the specified title within the specified event.
     *
     * @param title         title of a track
     * @param parentEventId parent event id of a track
     * @return - tracks within this event if exists, else null
     */
    public Track getTrackByTitleInEvent(Title title, long parentEventId) throws IllegalArgumentException, NoSuchElementException {
        if (parentEventId < 0) {
            throw new IllegalArgumentException("Invalid parent id");
        }
        if (title == null || title.toString() == null) {
            throw new IllegalArgumentException("Null reference for track title");
        }
        Optional<Track> track = trackRepository.findByTitleAndParentEventId(title, parentEventId);
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with title:" + title.toString()
                    + " does not exist  in event: " + parentEventId);
        }
        return track.get();
    }

}
