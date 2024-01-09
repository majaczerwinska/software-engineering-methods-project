package nl.tudelft.sem.template.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.domain.user.Link;


public class LinkAttributeConverter implements AttributeConverter<Link, String> {
    @Override
    public String convertToDatabaseColumn(Link attribute) {
        return attribute.toString();
    }

    @Override
    public Link convertToEntityAttribute(String dbData) {
        return new Link(dbData);
    }
}
