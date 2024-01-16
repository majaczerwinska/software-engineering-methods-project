package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.AttributeConverter;


/**
 * JPA Converter for the Communication value object.
 */
public class CommunicationAttributeConverter implements AttributeConverter<Communication, String> {
    @Override
    public String convertToDatabaseColumn(Communication attribute) {
        return Objects.toString(attribute);
    }

    @Override
    public Communication convertToEntityAttribute(String dbData) {
        return new Communication(dbData);
    }
}
