package nl.tudelft.sem.template.domain.track;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting track aggregate roots.
 */
@Repository
public interface TrackRepository extends JpaRepository<Track, String> {

    /** save th track in to db.
     *
     * @return the save the track with id
     */
    Track save(Track track);

    /**
     * find track that have this id.
     *
     * @param id the id to look for
     * @return track that have this id
     */
    Optional<Track> findById(Long id);

    /**
     * find track that have this title in the input event.
     *
     * @param title     the title to look for
     * @param eventId     the event where the track belongs to
     * @return track that have this title in the input event
     */
    Optional<Track> findByTitleAndEventId(Title title, Long eventId);

    /**
     * find list of track that have this title.
     *
     * @param title the title to look for
     * @return list of track that have this title
     */
    List<Track> findByTitle(Title title);

    /**
     * find list of track that in the event.
     *
     * @param eventId the event id where the track belongs to
     * @return list of track that in the event
     */
    List<Track> findByEventId(Long eventId);

    /**
     * check if event with input id exist.
     *
     * @param id the id to check for
     * @return true if event with input id exist
     */
    boolean existsById(Long id);

    /**
     * check if event with input title exist in the input event.
     *
     * @param title             the title to check for
     * @param eventId     the event to check for
     * @return true if event with input title exist in the input event
     */
    boolean existsByTitleAndEventId(Title title, Long eventId);

    /**
     * check if event with input title exist.
     *
     * @param title the title to check for
     * @return true if event with input title exist
     */
    boolean existsByTitle(Title title);
}
