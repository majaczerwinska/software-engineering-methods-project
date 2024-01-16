package nl.tudelft.sem.template.domain.attendee;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting Attendee aggregate roots.
 */
@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    Optional<Attendee> findByUserAndEventAndTrackAndConfirmation(AppUser user,
                                                                 Event event,
                                                                 Track track,
                                                                 Confirmation confirmation);

    List<Attendee> findByConfirmation(Confirmation confirmation);

    boolean existsByUserIdAndEventIdAndTrackId(Long userId, Long eventId, Long trackId);

    boolean existsByUserIdAndEventIdAndTrackIdAndConfirmation(Long userId, Long eventId, Long trackId,
            Confirmation confirmed);

    /**
     * A customized retrieval that filters on the basis of users, events,
     * or track. The filtration occur only if a non-null filter is provided;
     * otherwise, the filter is ignored.
     *
     * @param user              the user filter
     * @param event             the event filter
     * @param track             the track filter
     * @param confirmation      the confirmation status filter
     * @return                  all attendees that pass all the filters.
     *
     *
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE (:user is null OR a.user = :user) "
            + "AND (:event is null OR a.event = :event) "
            + "AND (:track is null OR a.track = :track) "
            + "AND (:confirmation is null OR a.confirmation = :confirmation)")
    List<Attendee> findFiltered(@Param("user") AppUser user,
                                @Param("event") Event event,
                                @Param("track") Track track,
                                @Param("confirmation") Confirmation confirmation);
}
