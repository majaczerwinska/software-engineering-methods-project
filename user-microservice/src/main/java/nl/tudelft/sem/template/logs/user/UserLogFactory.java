package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.logs.LogFactory;


/**
 * The factory implementation for the AppUser ```LogFactory```.
 */
public class UserLogFactory extends LogFactory {

    /**
     * Creates a Log representing the edition of a user's affiliation.
     *
     * @param subject the newly created user.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserAffiliationChange(AppUser subject) {
        return new UserAffiliationChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the edition of a user's list of attendees (attendance).
     *
     * @param subject the newly created user.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserAttendanceChange(AppUser subject) {
        return new UserAttendanceChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the edition of a user's communication.
     *
     * @param subject the newly created user.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserCommunicationChange(AppUser subject) {
        return new UserCommunicationChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the modification of a user's personal website (link).
     *
     * @param subject user that was modified.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserLinkChange(AppUser subject) {
        return new UserLinkChangedEventLog(subject);
    }

    /**
     * Creates a Log representing the deletion of a user.
     *
     * @param subject user that was deleted.
     * @return a Log representing the deletion of the user.
     */
    public UserLog registerUserDeletion(AppUser subject) {
        return new UserDeletedEventLog(subject);
    }



}
