package nl.tudelft.sem.template.domain.event;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the IsCancelled value object.
 */
public class IsCancelledAttributeConverter implements AttributeConverter<IsCancelled, String> {
    @Override
    public String convertToDatabaseColumn(IsCancelled attribute) {
        return (attribute.getCancelStatus()) ? "true" : "false";
    }

    @Override
    public IsCancelled convertToEntityAttribute(String dbData) {
        return (dbData.charAt(0) == 't') ? new IsCancelled(true) : new IsCancelled(false);
    }
}
