package nl.tudelft.sem.template.domain.user.attributeConverters;

import nl.tudelft.sem.template.domain.user.Name;

import javax.persistence.AttributeConverter;

public class NameAttributeConverter implements AttributeConverter<Name, String> {
    @Override
    public String convertToDatabaseColumn(Name attribute) {
        return attribute.toString();
    }

    @Override
    public Name convertToEntityAttribute(String dbData) {
        return new Name(dbData);
    }
}
