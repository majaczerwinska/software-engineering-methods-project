package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserName {
    private final transient String userName;

    public UserName(String userName) {
        // validate NetID
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}
