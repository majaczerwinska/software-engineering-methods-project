package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Link {
    private final transient String userLink;

    public Link(String userLink) {
        // validate NetID
        this.userLink = userLink;
    }

    @Override
    public String toString() {
        return userLink;
    }
}
