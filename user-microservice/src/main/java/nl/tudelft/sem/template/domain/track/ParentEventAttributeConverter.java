package nl.tudelft.sem.template.domain.track;

import nl.tudelft.sem.template.domain.event.Event;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the parent event value object.
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