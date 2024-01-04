package nl.tudelft.sem.template.domain.attendee;

import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * A DDD repository for querying and persisting Attendee aggregate roots.
 */
@Repository
//The redundancy of literals here is justified for the queries and the parameter alias.
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {


    /**
     * Returns TRUE if and only if the Attendee instance corresponding to the given
     * identifiers exists in the database. Otherwise, returns false.
     *
     * <p>This method is useful to 'quickly' check whether an instance exists in the
     * database without having to directly retrieve that particular instance.
     *
     * <p>This method does not handle nullable track identifiers.
     *
     * @param userId  the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier, not nullable
     * @return Returns TRUE if such a confirmed Attendee instance exists; otherwise,
     *          returns false.
     */
    @Query("SELECT count(a) > 0 FROM Attendee a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId = :trackId")
    boolean exists(@Param("userId") Long userId,
                   @Param("eventId") Long eventId,
                   @Param("trackId") @NonNull Long trackId);

    /**
     * Returns TRUE if and only if the Attendee instance corresponding to the given
     * identifiers exists in the database. Otherwise, returns false.
     *
     * <p>This method is useful to 'quickly' check whether an instance exists in the
     * database without having to directly retrieve that particular instance.
     *
     * <p>This method should be called when dealing with a null track identifiers. This
     * method assumes that the track identifier is null.
     *
     * @param userId  the user identifier
     * @param eventId the event identifier
     * @return Returns TRUE if such a confirmed Attendee instance exists; otherwise,
     *          returns false.
     */
    @Query("SELECT count(a) > 0 FROM Attendee a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId IS NULL")
    boolean exists(@Param("userId") Long userId,
                   @Param("eventId") Long eventId);

    /**
     * Returns TRUE if and only if an Attendee instance exists with the given identifiers
     * and the given confirmation status (not an invitation status).
     *
     * <p>Otherwise, returns FALSE. This method is useful to 'quickly' check whether an
     * instance exists in the database without having to directly retrieve that particular
     * instance.
     *
     * <p>This method does not handle nullable track identifiers.
     *
     * @param userId  the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier, not nullable
     * @param confirmation the confirmation
     * @return Returns TRUE if such a confirmed Attendee instance exists;
     *          otherwise, returns false.
     */
    @Query("SELECT count(a) > 0 FROM Attendee a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId = :trackId AND a.confirmation = :confirmation")
    boolean existsConfirmed(@Param("userId") Long userId,
                            @Param("eventId") Long eventId,
                            @Param("trackId") @NonNull Long trackId,
                            @Param("confirmation") Confirmation confirmation);

    /**
     * Returns TRUE if and only if an Attendee instance exists with the given identifiers
     * and the given confirmation status (not an invitation status).
     * Otherwise, returns FALSE.
     *
     * <p>This method is useful to 'quickly' check whether an instance exists in the database
     * without having to directly retrieve that particular instance.
     *
     * <p>This method should be called when dealing with a null track identifiers. This
     * method assumes that the track identifier is null.
     *
     * @param userId  the user identifier
     * @param eventId the event identifier
     * @param confirmation the confirmation
     * @return Returns TRUE if such a confirmed Attendee instance exists; otherwise, returns FALSE
     */
    @Query("SELECT count(a) > 0 FROM Attendee a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId IS null AND a.confirmation = :confirmation")
    boolean existsConfirmed(@Param("userId") Long userId,
                            @Param("eventId") Long eventId,
                            @Param("confirmation") Confirmation confirmation);

    /**
     * Optionally retrieves the Attendee instance corresponding to the given identifiers.
     *
     * <p>This method should be called when dealing with a null track identifiers. This
     * method assumes that the track identifier is null.
     *
     * @param userId the user identifier
     * @param eventId the event identifier
     * @param trackId the track identifier, not nullable
     * @return an optional Attendee instance
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId = :trackId")
    Optional<Attendee> find(@Param("userId") Long userId,
                            @Param("eventId") Long eventId,
                            @Param("trackId") @NonNull Long trackId);

    /**
     * Optionally retrieves the Attendee instance corresponding to the given identifiers.
     *
     * <p>This method does not handle nullable track identifiers.
     *
     * @param userId the user identifier
     * @param eventId the event identifier
     * @return an optional Attendee instance
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE a.userId = :userId AND a.eventId = :eventId "
            + "AND a.trackId IS NULL")
    Optional<Attendee> find(@Param("userId") Long userId,
                            @Param("eventId") Long eventId);

    /**
     * Returns the list of all Attendee instances that either are or are not confirmed.
     *
     * @param confirmed the confirmation status of attendance with the given role
     * @return a list of Attendee instances
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE a.confirmation = :confirmed")
    List<Attendee> find(@Param("confirmed") Confirmation confirmed);

    /**
     * Returns the list of all Attendee instances that are related to the given
     * user identifier. Additionally, this method filters the instanced on the
     * basis of whether they were confirmed; in other words, the method can filter
     * to provide only all pending invitations.
     *
     * @param userId    the user identifier
     * @param confirmed the confirmation status of attendance with the given role
     * @return a list of Attendee instances
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE a.userId = :userId AND a.confirmation = :confirmed")
    List<Attendee> findByUser(@Param("userId") long userId,
                              @Param("confirmed") Confirmation confirmed);

    /**
     * Returns the list of all Attendee instances that are related to the given
     * event identifier. Additionally, this method filters the instanced on the
     * basis of whether they were confirmed; in other words, the method can filter
     * to provide only all pending invitations.
     *
     * @param eventId   the event identifier
     * @param confirmed the confirmation status of attendance with the given role
     * @return a list of Attendee instances
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE a.eventId = :eventId AND a.confirmation = :confirmed")
    List<Attendee> findByEvent(@Param("eventId") Long eventId,
                               @Param("confirmed") Confirmation confirmed);

    /**
     * Returns the list of all Attendee instances that are related to the given
     * track identifier. Additionally, this method filters the instanced on the
     * basis of whether they were confirmed; in other words, the method can filter
     * to provide only all pending invitations.
     *
     * @param trackId   the track identifier, not nullable
     * @param confirmed the confirmation status of attendance with the given role
     * @return a list of Attendee instances
     */
    @Query("SELECT a FROM Attendee a "
            + "WHERE a.trackId = :trackId AND a.confirmation = :confirmed")
    List<Attendee> findByTrack(@Param("trackId") @NonNull Long trackId,
                               @Param("confirmed") Confirmation confirmed);

    /**
     * Returns the number of attending General Chairs in the given event.
     *
     * <p>This method is a useful utility to 'quickly' query the total number in order
     * to verify particular capacity constraints.
     *
     * @param eventId the event identifier
     * @return the total number of attending General Chairs
     */
    @Query("SELECT count(a) FROM Attendee a "
            + "WHERE a.eventId = :eventId AND a.role = 'GENERAL_CHAIR'")
    long countGeneralChairs(@Param("eventId") Long eventId);
}
