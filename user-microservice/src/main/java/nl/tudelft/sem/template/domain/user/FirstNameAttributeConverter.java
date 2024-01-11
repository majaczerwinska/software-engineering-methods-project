package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

public class FirstNameAttributeConverter implements AttributeConverter<FirstName, String> {

    @Override
    public String convertToDatabaseColumn(FirstName attribute) {
        return attribute.toString();
    }

    @Override
    public FirstName convertToEntityAttribute(String dbData) {
        return new FirstName(dbData);
    }
}
