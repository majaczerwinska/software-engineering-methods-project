package nl.tudelft.sem.template.domain.track;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting user aggregate roots.
 */
@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
    /**
     * find track that have this id.
     *
     * @param id the id to look for
     * @return track that have this id
     */
    @Query("SELECT t FROM Track t WHERE t.id = :id")
    Optional<Track> findById(@Param("id") String id);

    /**
     * find track that have this title in the input event.
     *
     * @param t     the title to look for
     * @param event the event where the track belongs to
     * @return track that have this title in the input event
     */
    @Query("SELECT t FROM Track t WHERE t.title = :ti AND t.event = :event")
    Optional<Track> findByTitleAndEvent(@Param("ti") String t, @Param("event") Event event);


    /**
     * find list of track that have this title.
     *
     * @param t     the title to look for
     * @return list of track that have this title
     */
    @Query("SELECT t FROM Track t WHERE t.title = :ti")
    List<Track> findByTitle(@Param("ti") String t);

    /**
     * find list of track that have submission deadline before the input date.
     *
     * @param date the latest date for giving submission
     * @return list of track that have submission deadline before the input date
     */
    @Query("SELECT t FROM Track t WHERE t.submitDeadline < :latestDate")
    List<Track> findBySubmitDeadline(@Param("latestDate") Date date);

    /**
     * find list of track that have review deadline before the input date.
     *
     * @param date the latest date for giving review
     * @return list of track that have review deadline before the input date
     */
    @Query("SELECT t FROM Track t WHERE t.reviewDeadline < :date")
    List<Track> findByReviewDeadline(@Param("date") Date date);

    /**
     * find list of track that have the keyword in its description.
     *
     * @param keyword the word that should be i the description
     * @return list of track that have the keyword in its description
     */
    @Query("SELECT t FROM Track t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Track> findByDescriptionContaining(@Param("keyword") String keyword);

    /**
     * find list of track that in the event.
     *
     * @param event the event where the track belongs to
     * @return list of track that in the event
     */
    @Query("SELECT t FROM Track t WHERE t.event = :event")
    List<Track> findByEvent(@Param("event") Event event);

    /**
     * check if event with input id exist.
     *
     * @param id the id to check for
     * @return true if event with input id exist
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Track t WHERE t.id = :id")
    boolean existsById(@Param("id") long id);

    /**
     * check if event with input title exist in the input event.
     *
     * @param t     the title to check for
     * @return true if event with input title exist in the input event
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Track t WHERE t.title = :title AND t.event = :event")
    boolean existsByTitleInEvent(@Param("title") String t, @Param("event") Event event);

    /**
     * check if event with input title exist.
     *
     * @param t     the title to check for
     * @return true if event with input title exist
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Track t WHERE t.title = :title")
    boolean existsByTitle(@Param("title") String t);
}
