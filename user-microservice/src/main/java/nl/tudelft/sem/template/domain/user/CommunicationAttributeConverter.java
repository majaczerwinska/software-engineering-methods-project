package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

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
