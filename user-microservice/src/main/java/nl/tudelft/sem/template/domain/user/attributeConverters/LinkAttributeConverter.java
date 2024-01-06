package nl.tudelft.sem.template.domain.user.attributeConverters;

import nl.tudelft.sem.template.domain.user.Link;

import javax.persistence.AttributeConverter;

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
