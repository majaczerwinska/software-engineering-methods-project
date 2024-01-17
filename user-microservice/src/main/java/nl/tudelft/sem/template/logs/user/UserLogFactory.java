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
