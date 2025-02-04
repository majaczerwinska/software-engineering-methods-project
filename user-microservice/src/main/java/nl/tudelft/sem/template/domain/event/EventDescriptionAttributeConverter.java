package nl.tudelft.sem.template.domain.event;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the EventDescription value object.
 */
public class EventDescriptionAttributeConverter implements AttributeConverter<EventDescription, String> {

    @Override
    public String convertToDatabaseColumn(EventDescription attribute) {
        return attribute.toString();
    }

    @Override
    public EventDescription convertToEntityAttribute(String dbData) {
        return new EventDescription(dbData);
    }
}
