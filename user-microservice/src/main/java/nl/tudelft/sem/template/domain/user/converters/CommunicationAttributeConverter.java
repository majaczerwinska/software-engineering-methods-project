package nl.tudelft.sem.template.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.domain.user.Communication;

/**
 * JPA Converter for the Communication value object.
 */
public class CommunicationAttributeConverter implements AttributeConverter<Communication, String> {
    @Override
    public String convertToDatabaseColumn(Communication attribute) {
        return attribute.toString();
    }

    @Override
    public Communication convertToEntityAttribute(String dbData) {
        return new Communication(dbData);
    }
}
