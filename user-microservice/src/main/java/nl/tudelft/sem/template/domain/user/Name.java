package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Name {
    private final transient String userName;

    public Name(String userName) {
        // validate NetID
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}
