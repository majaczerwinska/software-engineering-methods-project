package nl.tudelft.sem.template.example.domain.attendee;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
//The redundancy of literals here is justified for the queries and the parameter alias.
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public interface AttendeeRepository extends JpaRepository<Attendee, AttendeeId> {

    /*  List of available methods (for your convenience :^) )
        -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
            1. exists(uID,eID,tID)
            2. find(uID,eID,tID)
            3. findByUser(uID,isConfirmed)
            4. findByEvent(eID,isConfirmed)
            5. findByTrack(tID,isConfirmed)
            6. countGeneralChairs(eID)
     */

    /**
     * Returns TRUE if and only if an Attendee instance exists with the given composite
     * primary key valuation. Otherwise, returns FALSE. This method is useful to 'quickly'
     * check whether an instance exists in the database without having to directly retrieve
     * that particular instance.
     *
     * @param userId  the user identifier.
     * @param eventId the event identifier.
     * @param trackId the track identifier.
     * @return TRUE if such an Attendee instance exists.
     */
    @Query("SELECT count(*) > 0 FROM attendees AS a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId = :trackId")
    boolean exists(@Param("userId") Long userId,
                   @Param("eventId") Long eventId,
                   @Param("trackId") Long trackId);


    @Query("SELECT * FROM attendees AS a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId = :trackId")
    Optional<Attendee> find(@Param("userId") Long userId,
                            @Param("eventId") Long eventId,
                            @Param("trackId") Long trackId);

    /**
     * Returns the list of all Attendee instances that are related to the given
     * user identifier. Additionally, this method filters the instanced on the
     * basis of whether they were confirmed; in other words, the method can filter
     * to provide only all pending invitations.
     *
     * @param userId    the user identifier.
     * @param confirmed the confirmation status of attendance with the given role.
     * @return a list of Attendee instances.
     */
    @Query("SELECT * FROM attendees AS a "
            + "WHERE a.userId = :userId AND a.confirmed = :confirmed")
    List<Attendee> findByUser(@Param("userId") Long userId,
                              @Param("confirmed") Boolean confirmed);

    /**
     * Returns the list of all Attendee instances that are related to the given
     * event identifier. Additionally, this method filters the instanced on the
     * basis of whether they were confirmed; in other words, the method can filter
     * to provide only all pending invitations.
     *
     * @param eventId   the event identifier.
     * @param confirmed the confirmation status of attendance with the given role.
     * @return a list of Attendee instances.
     */
    @Query("SELECT * FROM attendees AS a "
            + "WHERE a.eventId = :eventId AND a.confirmed = :confirmed")
    List<Attendee> findByEvent(@Param("eventId") Long eventId,
                               @Param("confirmed") Boolean confirmed);

    /**
     * Returns the list of all Attendee instances that are related to the given
     * track identifier. Additionally, this method filters the instanced on the
     * basis of whether they were confirmed; in other words, the method can filter
     * to provide only all pending invitations.
     *
     * @param trackId   the user identifier.
     * @param confirmed the confirmation status of attendance with the given role.
     * @return a list of Attendee instances.
     */
    @Query("SELECT * FROM attendees AS a "
            + "WHERE a.trackId = :track AND a.confirmed = :confirmed")
    List<Attendee> findByTrack(@Param("trackId") Long trackId,
                               @Param("confirmed") Boolean confirmed);

    /**
     * Returns the number of attending General Chairs in the given event.
     * This method is a useful utility to 'quickly' query the total number in order
     * to verify particular capacity constraints.
     *
     * @param eventId the event identifier.
     * @return the total number of attending General Chairs.
     */
    @Query("SELECT count(*) FROM attendees AS a "
            + "WHERE a.eventId = :eventId AND a.role = 'GENERAL_CHAIR'")
    long countGeneralChairs(@Param("eventId") Long eventId);
}
