package nl.tudelft.sem.template.example.domain.attendee;

import javax.persistence.AttributeConverter;

public class ConfirmationAttributeConverter implements AttributeConverter<Confirmation, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(Confirmation attribute) {
        return attribute.getConfirmed();
    }

    @Override
    public Confirmation convertToEntityAttribute(Boolean dbData) {
        return new Confirmation(dbData);
    }
}
