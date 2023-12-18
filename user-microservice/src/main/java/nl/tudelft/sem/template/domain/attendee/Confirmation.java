package nl.tudelft.sem.template.domain.attendee;

import lombok.Getter;

@Getter
public class Confirmation {

    private final transient boolean confirmed;

    public Confirmation(boolean confirmed) {
        this.confirmed = confirmed;
    }

}
