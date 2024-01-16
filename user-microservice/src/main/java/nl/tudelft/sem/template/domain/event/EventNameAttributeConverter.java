package nl.tudelft.sem.template.domain.event;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the EventName value object.
 */
@Converter
public class EventNameAttributeConverter implements AttributeConverter<EventName, String> {

    @Override
    public String convertToDatabaseColumn(EventName attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public EventName convertToEntityAttribute(String dbData) {
        return new EventName(dbData);
    }

}
