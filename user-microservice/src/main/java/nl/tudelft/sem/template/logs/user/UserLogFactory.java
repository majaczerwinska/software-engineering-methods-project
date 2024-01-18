package nl.tudelft.sem.template.logs.user;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.logs.LogFactory;


/**
 * The factory implementation for the AppUser ```LogFactory```.
 */
public class UserLogFactory extends LogFactory {

    public UserLog registerCreation(AppUser subject) {
        return new CreatedUserLog(subject);
    }

    /**
     * Creates a Log representing the edition of a user's affiliation.
     *
     * @param subject the newly created user.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserAffiliationChange(AppUser subject) {
        return new UserAffiliationChangedUserLog(subject);
    }

    /**
     * Creates a Log representing the edition of a user's list of attendees (attendance).
     *
     * @param subject the newly created user.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserAttendanceChange(AppUser subject) {
        return new UserAttendanceChangedUserLog(subject);
    }

    /**
     * Creates a Log representing the edition of a user's communication.
     *
     * @param subject the newly created user.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserCommunicationChange(AppUser subject) {
        return new UserCommunicationChangedUserLog(subject);
    }

    /**
     * Creates a Log representing the modification of a user's personal website (link).
     *
     * @param subject user that was modified.
     * @return a Log representing the modification of the user.
     */
    public UserLog registerUserLinkChange(AppUser subject) {
        return new UserLinkChangedUserLog(subject);
    }

    public UserLog registerEmailChange(AppUser subject) {
        return new EmailChangedUserLog(subject);
    }

    public UserLog registerFirstNameChange(AppUser subject) {
        return new FirstNameChangedUserLog(subject);
    }

    public UserLog registerLastNameChange(AppUser subject) {
        return new LastNameChangedUserLog(subject);
    }
}
