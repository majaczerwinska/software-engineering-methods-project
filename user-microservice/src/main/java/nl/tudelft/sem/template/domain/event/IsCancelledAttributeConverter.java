package nl.tudelft.sem.template.domain.event;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the IsCancelled value object.
 */
public class IsCancelledAttributeConverter implements AttributeConverter<IsCancelled, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(IsCancelled attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCancelStatus();
    }

    @Override
    public IsCancelled convertToEntityAttribute(Boolean dbData) {
        return new IsCancelled(dbData);
    }
}
