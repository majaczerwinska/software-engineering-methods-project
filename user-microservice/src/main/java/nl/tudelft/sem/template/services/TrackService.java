package nl.tudelft.sem.template.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.track.ParentEvent;
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
        } else if (track.getTitle() == null) {
            throw new IllegalArgumentException("Null reference for track title");
        } else if (trackRepository.existsById(String.valueOf(track.getId()))) {
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
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid track id: " + id);
        }
        if (!trackExistById(id)) {
            throw new NoSuchElementException("Track with id:" + id + " does not exist.");
        }
        Track track = getTrackById(id);
        trackRepository.delete(track);
        return track;
    }

    /**
     * Updates an existing track account and saves it in the repository.
     *
     * @param track - the updated track account to be saved.
     * @return the updated track that was saved.
     */
    @Transactional
    public Track updateTrack(Track track) throws IllegalArgumentException, NoSuchElementException {
        if (track.getTitle() == null) {
            throw new IllegalArgumentException("Null reference for track title");
        } else if (trackExistsByTitleInEvent(track.getTitle(), track.getEvent())) {
            throw new IllegalArgumentException("Track with this title already exist in the event.");
        } else if (!trackExistById(track.getId())) {
            throw new NoSuchElementException("Track with id:" + track.getId().toString() + " does not exist.");
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
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid track ID");
        }
        Optional<Track> track = trackRepository.findById(String.valueOf(id));
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with ID: " + id + " does not exist.");
        }

        return track.get();
    }

    /**
     * Retrieves the track with the specified id.
     *
     * @param id - id of a track
     * @return - track with this id if exists, else null
     */
    public boolean trackExistById(long id) {
        return trackRepository.existsById(id);
    }

    /**
     * Retrieves all tracks with the specified title.
     *
     * @param title - title of a track
     * @return - tracks with this title if exists, else null
     */
    public List<Track> getTrackByTitle(Title title) throws NoSuchElementException {
        List<Track> tracks = trackRepository.findByTitle(title);
        if (tracks.isEmpty()) {
            throw new NoSuchElementException("Track with title:" + title.toString() + " can not be found.");
        }
        return tracks;
    }

    /**
     * Checks whether the track with the specified title exists.
     *
     * @param title - title of a track
     * @return - true if exists, false otherwise
     */
    public boolean trackExistsByTitle(Title title) {
        return trackRepository.existsByTitle(title);
    }


    /**
     * Retrieves all tracks within the specified event.
     *
     * @param parentEvent - parent event of a track
     * @return - tracks within this event if exists, else null
     */
    public List<Track> getTrackByParentEvent(ParentEvent parentEvent) throws NoSuchElementException {
        List<Track> tracks = trackRepository.findByEvent(parentEvent);
        if (tracks.isEmpty()) {
            throw new NoSuchElementException("Track does not exist in event:" + parentEvent.toEvent().getName());
        }
        return tracks;
    }

    /**
     * Retrieves track with the specified title within the specified event.
     *
     * @param title       - title of a track
     * @param parentEvent - parent event of a track
     * @return - tracks within this event if exists, else null
     */
    public Track getTrackByTitleInEvent(Title title, ParentEvent parentEvent) throws NoSuchElementException {
        Optional<Track> track = trackRepository.findByTitleAndEvent(title, parentEvent);
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with title:" + title.toString()
                    + " does not exist  in event: " + parentEvent.toEvent().getName());
        }
        return track.get();
    }

    /**
     * Checks whether the track with the specified title exists within the specified event.
     *
     * @param title       - title of a track
     * @param parentEvent - parent event of a track
     * @return - true if exists, false otherwise
     */
    public boolean trackExistsByTitleInEvent(Title title, ParentEvent parentEvent) {
        return trackRepository.existsByTitleInEvent(title, parentEvent);
    }

}
