package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserLink {
    private final transient String userLink;

    public UserLink(String userLink) {
        // validate NetID
        this.userLink = userLink;
    }

    @Override
    public String toString() {
        return userLink;
    }
}
