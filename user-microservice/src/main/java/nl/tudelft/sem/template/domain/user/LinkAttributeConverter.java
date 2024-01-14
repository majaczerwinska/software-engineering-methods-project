package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the Link value object.
 */
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
