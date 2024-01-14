package nl.tudelft.sem.template.domain.event;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Override
    List<Event> findAll();

    /**
     * idk I thought we may want this type of methods for filtering the events, but
     * that would mean
     * that events are associated with a user. This would imply adding a userId
     * field in the Event class or
     * some relationship mapping in the Event class.
     */
    // List<Event> findAllByUserId(Long id);

    // Unfortunately a custom query is unavoidable here :(
    @Query("SELECT e"
            + " FROM Event e"
            + " WHERE (:startBefore IS NULL"
            + "        OR e.startDate < :startBefore)"
            + "   AND (:startAfter IS NULL"
            + "        OR e.startDate > :startAfter)"
            + "   AND (:endBefore IS NULL"
            + "        OR e.endDate < :endBefore)"
            + "   AND (:endAfter IS NULL"
            + "        OR e.endDate > :endAfter)"
            + "   AND (:cancelled IS NULL"
            + "        OR e.isCancelled = :cancelled)"
            + "   AND (:name IS NULL"
            + "        OR e.name = :name)")
    List<Event> findByOptionalParams(LocalDate startBefore,
            LocalDate startAfter,
            LocalDate endBefore,
            LocalDate endAfter,
            IsCancelled cancelled,
            EventName name);

    boolean existsById(Long id);
}
