package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.AttributeConverter;



public class NameAttributeConverter implements AttributeConverter<Name, String> {
    @Override
    public String convertToDatabaseColumn(Name attribute) {
        return Objects.toString(attribute);
    }

    @Override
    public Name convertToEntityAttribute(String dbData) {
        return new Name(dbData);
    }
}
