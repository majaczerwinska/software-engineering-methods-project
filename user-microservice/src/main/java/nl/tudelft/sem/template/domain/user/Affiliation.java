package nl.tudelft.sem.template.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Affiliation {
    private final transient String affiliation;

    public Affiliation(String affiliation) {
        // validate NetID
        this.affiliation = affiliation;
    }

    @Override
    public String toString() {
        return affiliation;
    }
}
