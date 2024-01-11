package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

public class LastNameAttributeConverter implements AttributeConverter<LastName, String> {

    @Override
    public String convertToDatabaseColumn(LastName attribute) {
        return attribute.toString();
    }

    @Override
    public LastName convertToEntityAttribute(String dbData) {
        return new LastName(dbData);
    }
}
