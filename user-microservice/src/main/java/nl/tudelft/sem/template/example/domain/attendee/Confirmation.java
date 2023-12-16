package nl.tudelft.sem.template.example.domain.attendee;

import lombok.Getter;

@Getter
public class Confirmation {

    private final transient Boolean confirmed;

    public Confirmation(Boolean confirmed) {
        this.confirmed = confirmed;
    }

}
