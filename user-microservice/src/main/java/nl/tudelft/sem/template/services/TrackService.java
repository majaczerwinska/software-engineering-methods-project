package nl.tudelft.sem.template.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.track.Description;
import nl.tudelft.sem.template.domain.track.PaperRequirement;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.model.PaperType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A DDD service for track.
 */
@Service
public class TrackService {
    private final transient TrackRepository trackRepository;
    private final transient EventRepository eventRepository;

    /**
     * A constructor dependency injection for the track JPA Repository concrete
     * implementation.
     *
     * @param trackRepository the track repository injection
     */
    @Autowired
    public TrackService(TrackRepository trackRepository, EventRepository eventRepository) {
        this.trackRepository = trackRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Creates a new track.
     *
     * @return - the created track
     */
    @Transactional
    public Track createTrack(String title, String description, LocalDate submitDeadline, LocalDate reviewDeadline,
            PaperType paperType, Long eventId) throws IllegalArgumentException {
        Event event = eventRepository.findById(eventId).get();
        Track track = new Track(new Title(title), new Description(description), new PaperRequirement(paperType),
                submitDeadline, reviewDeadline, event);
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
     * @return the updated track that was saved.
     */
    @Transactional
    public Track updateTrack(Long id, String title, String description, LocalDate submitDeadline,
            LocalDate reviewDeadline, PaperType paperType, Long eventId)
            throws IllegalArgumentException, NoSuchElementException {
        Optional<Track> inDb = trackRepository.findByTitleAndEventId(new Title(title), eventId);
        if (inDb.isPresent() && inDb.get().getId() != id) {
            throw new IllegalArgumentException("Track with this title already exist in the event.");
        }
        if (trackRepository.existsById(id)) {
            throw new NoSuchElementException("Track with id:" + id.toString() + " does not exist.");
        }

        Event event = eventRepository.findById(eventId).get();
        Track track = new Track(id, new Title(title), new Description(description), new PaperRequirement(paperType),
                submitDeadline, reviewDeadline, event);
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
    public List<Track> getTrackByParentEvent(Long parentEventId)
            throws NoSuchElementException, IllegalArgumentException {
        if (parentEventId == null) {
            throw new IllegalArgumentException("Null reference for parent event");
        }
        List<Track> tracks = trackRepository.findByEventId(parentEventId);
        if (tracks.isEmpty()) {
            throw new NoSuchElementException("Track does not exist in event:" + parentEventId);
        }
        return tracks;
    }

    /**
     * Retrieves track with the specified title within the specified event.
     *
     * @param title         - title of a track
     * @param parentEventId - parent event id of a track
     * @return - tracks within this event if exists, else null
     */
    public Track getTrackByTitleInEvent(Title title, long parentEventId) throws NoSuchElementException {
        Optional<Track> track = trackRepository.findByTitleAndEventId(title, parentEventId);
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with title:" + title.toString()
                    + " does not exist  in event: " + parentEventId);
        }
        return track.get();
    }
}
