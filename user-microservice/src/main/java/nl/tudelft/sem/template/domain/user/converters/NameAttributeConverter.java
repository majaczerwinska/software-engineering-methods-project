package nl.tudelft.sem.template.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.domain.user.Name;


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
