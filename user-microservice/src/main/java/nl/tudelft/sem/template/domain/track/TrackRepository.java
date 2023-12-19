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
    /**
     * find track that have this id.
     *
     * @param id the id to look for
     * @return track that have this id
     */
    Optional<Track> findById(String id);

    /**
     * find track that have this title in the input event.
     *
     * @param title     the title to look for
     * @param event the event where the track belongs to
     * @return track that have this title in the input event
     */
    Optional<Track> findByTitleAndEvent(Title title, ParentEvent event);

    /**
     * find list of track that have this title.
     *
     * @param title     the title to look for
     * @return list of track that have this title
     */
    List<Track> findByTitle(Title title);

    /**
     * find list of track that in the event.
     *
     * @param event the event where the track belongs to
     * @return list of track that in the event
     */
    List<Track> findByEvent(ParentEvent event);

    /**
     * check if event with input id exist.
     *
     * @param id the id to check for
     * @return true if event with input id exist
     */
    boolean existsById(long id);

    /**
     * check if event with input title exist in the input event.
     *
     * @param title     the title to check for
     * @return true if event with input title exist in the input event
     */
    boolean existsByTitleInEvent(Title title, ParentEvent event);

    /**
     * check if event with input title exist.
     *
     * @param title     the title to check for
     * @return true if event with input title exist
     */
    boolean existsByTitle(Title title);
}
