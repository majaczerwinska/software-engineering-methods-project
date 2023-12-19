package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserAffiliation {
    private final transient String affiliation;

    public UserAffiliation(String affiliation) {
        // validate NetID
        this.affiliation = affiliation;
    }

    @Override
    public String toString() {
        return affiliation;
    }
}
