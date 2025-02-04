package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.AttributeConverter;



public class LinkAttributeConverter implements AttributeConverter<Link, String> {
    @Override
    public String convertToDatabaseColumn(Link attribute) {
        return Objects.toString(attribute);
    }

    @Override
    public Link convertToEntityAttribute(String dbData) {
        return new Link(dbData);
    }
}
