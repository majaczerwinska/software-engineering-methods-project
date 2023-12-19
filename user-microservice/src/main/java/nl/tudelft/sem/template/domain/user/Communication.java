package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Communication {
    private final transient String userCommunication;

    public Communication(String userCommunication) {
        // validate NetID
        this.userCommunication = userCommunication;
    }

    @Override
    public String toString() {
        return userCommunication;
    }
}
