package nl.tudelft.sem.template.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
// The redundancy of literals here is justified for the queries and the parameter alias.
@Service
public class TrackService {
    private final transient TrackRepository trackRepository;
    private final transient EventRepository eventRepository;
    private static final String nullTitle = "Null reference for track title";
    private static final String invalidId = "Invalid track id";
    private static final String invalidEventId = "Invalid event id";

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
        if (eventId < 0) {
            throw new IllegalArgumentException(invalidEventId);
        }
        if (title == null) {
            throw new IllegalArgumentException(nullTitle);
        }
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException(invalidEventId);
        }
        Track track = new Track(new Title(title), new Description(description), new PaperRequirement(paperType),
                submitDeadline, reviewDeadline, event.get());
        trackRepository.save(track);
        return track;
    }

    /**
     * Deletes the track with the specified id.
     *
     * @param id - id of the to be deleted track
     * @return - the deleted track
     */
    @Transactional
    public Track deleteTrackById(Long id) throws NoSuchElementException {
        if (id < 0) {
            throw new IllegalArgumentException(invalidId);
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
     * @return the updated track that was saved.
     */
    @Transactional
    public Track updateTrack(Long id, String title, String description, LocalDate submitDeadline,
            LocalDate reviewDeadline, PaperType paperType, Long eventId)
            throws IllegalArgumentException, NoSuchElementException {
        if (title == null) {
            throw new IllegalArgumentException(nullTitle);
        }
        Optional<Track> inDb = trackRepository.findByTitleAndEventId(new Title(title), eventId);
        if (inDb.isPresent() && !Objects.equals(inDb.get().getId(), id)) {
            throw new IllegalArgumentException("Track with this title already exist in the event.");
        }
        if (!trackRepository.existsById(id)) {
            throw new NoSuchElementException("Track with id:" + id + " does not exist.");
        }
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException(invalidEventId);
        }
        Track track = new Track(id, new Title(title), new Description(description), new PaperRequirement(paperType),
                submitDeadline, reviewDeadline, event.get());
        trackRepository.save(track);
        return track;
    }

    /**
     * Retrieves the track with the specified id.
     *
     * @param id - id of a track
     * @return - track with this id if exists, else null
     */
    public Track getTrackById(Long id) throws NoSuchElementException, IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException(invalidId);
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
            throw new IllegalArgumentException(nullTitle);
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
        if (parentEventId == null || parentEventId < 0) {
            throw new IllegalArgumentException(invalidEventId);
        }
        List<Track> tracks = trackRepository.findByEventId(parentEventId);
        if (tracks.isEmpty()) {
            throw new NoSuchElementException("No Track exist in event: " + parentEventId);
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
    public Track getTrackByTitleInEvent(Title title, Long parentEventId)
            throws IllegalArgumentException, NoSuchElementException {
        if (parentEventId < 0) {
            throw new IllegalArgumentException("Invalid parent id");
        }
        if (title == null || title.toString() == null) {
            throw new IllegalArgumentException(nullTitle);
        }
        Optional<Track> track = trackRepository.findByTitleAndEventId(title, parentEventId);
        if (track.isEmpty()) {
            throw new NoSuchElementException("Track with title:" + title.toString()
                    + " does not exist  in event: " + parentEventId);
        }
        return track.get();
    }

    /**
     * Determines whether the Track corresponding to the identifier
     * exists.
     *
     * @param id the Track identifier.
     * @return True if it exists.
     */
    @Transactional
    public boolean exists(Long id) {
        return trackRepository.existsById(id);
    }

}
