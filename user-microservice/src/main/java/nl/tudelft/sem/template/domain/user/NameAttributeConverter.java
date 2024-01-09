package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the Name value object.
 */
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
