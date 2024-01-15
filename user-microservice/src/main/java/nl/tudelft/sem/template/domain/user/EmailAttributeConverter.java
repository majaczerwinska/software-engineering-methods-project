package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/**
 * JPA Converter for the Email value object.
 */
@Converter
public class EmailAttributeConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return Objects.toString(attribute);
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return new Email(dbData);
    }

}
