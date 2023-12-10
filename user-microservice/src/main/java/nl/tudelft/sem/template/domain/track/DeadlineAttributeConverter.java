package nl.tudelft.sem.template.domain.track;

import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/**
 * JPA Converter for the Deadline value object.
 */
@Converter
public class DeadlineAttributeConverter implements AttributeConverter<Deadline, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(Deadline attribute) {
        return attribute.toDate();
    }

    @Override
    public Deadline convertToEntityAttribute(LocalDate dbData) {
        return new Deadline(dbData);
    }

}