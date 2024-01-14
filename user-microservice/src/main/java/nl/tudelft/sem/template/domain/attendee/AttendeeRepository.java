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
    Optional<Attendee> findByUserIdAndEventIdAndTrackId(Long userId, Long eventId, Long trackId);

    List<Attendee> findByUserIdAndConfirmation(Long userId, Confirmation confirmed);

    List<Attendee> findByEventIdAndConfirmation(Long eventId, Confirmation confirmed);

    List<Attendee> findByTrackIdAndConfirmation(Long trackId, Confirmation confirmed);

    boolean existsByUserIdAndEventIdAndTrackId(Long userId, Long eventId, Long trackId);

    boolean existsByUserIdAndEventIdAndTrackIdAndConfirmation(Long userId, Long eventId, Long trackId,
            Confirmation confirmed);
}
