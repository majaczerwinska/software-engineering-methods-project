package nl.tudelft.sem.template.domain.track;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.domain.Event;

/**
 * JPA Converter for the NetID value object.
 */
@Converter
public class ParentEventAttributeConverter implements AttributeConverter<ParentEvent, Event> {

    @Override
    public Event convertToDatabaseColumn(ParentEvent attribute) {
        return attribute.toEvent();
    }

    @Override
    public ParentEvent convertToEntityAttribute(Event dbData) {
        return new ParentEvent(dbData);
    }

}