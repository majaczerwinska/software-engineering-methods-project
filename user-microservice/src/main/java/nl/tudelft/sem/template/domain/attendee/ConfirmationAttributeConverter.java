package nl.tudelft.sem.template.domain.attendee;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the Confirmation value object.
 */
public class ConfirmationAttributeConverter implements AttributeConverter<Confirmation, String> {
    @Override
    public String convertToDatabaseColumn(Confirmation attribute) {
        if (attribute == null) {
            return null;
        }
        return (attribute.isConfirmed()) ? "true" : "false";
    }

    @Override
    public Confirmation convertToEntityAttribute(String dbData) {
        return (dbData.charAt(0) == 't') ? new Confirmation(true) : new Confirmation(false);
    }
}
