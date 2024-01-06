package nl.tudelft.sem.template.events;

import nl.tudelft.sem.template.domain.user.Email;

/**
 * A DDD domain event that indicated a user was created.
 */
public class UserWasCreatedEvent {
    private final Email email;

    public UserWasCreatedEvent(Email email) {
        this.email = email;
    }

    public Email getEmail() {
        return this.email;
    }
}
