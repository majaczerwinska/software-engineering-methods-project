package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserCommunication {
    private final transient String userCommunication;

    public UserCommunication(String userCommunication) {
        // validate NetID
        this.userCommunication = userCommunication;
    }

    @Override
    public String toString() {
        return userCommunication;
    }
}
