package nl.tudelft.sem.template.domain.attendee;

import javax.persistence.AttributeConverter;

public class ConfirmationAttributeConverter implements AttributeConverter<Confirmation, String> {
    @Override
    public String convertToDatabaseColumn(Confirmation attribute) {
        return (attribute.isConfirmed()) ? "true" : "false";
    }

    @Override
    public Confirmation convertToEntityAttribute(String dbData) {
        return (dbData.charAt(0) == 't') ? new Confirmation(true) : new Confirmation(false);
    }
}
