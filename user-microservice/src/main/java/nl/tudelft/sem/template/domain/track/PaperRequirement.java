package nl.tudelft.sem.template.domain.track;

import lombok.EqualsAndHashCode;


/**
 * A DDD value object representing the paper requirement in our domain.
 */
@EqualsAndHashCode
public class PaperRequirement {
    private final transient PaperType paperTypeValue;

    public PaperRequirement(PaperType paperType) {
        // Validate input
        this.paperTypeValue = paperType;
    }

    public PaperType toPaperType() {
        return paperTypeValue;
    }
}