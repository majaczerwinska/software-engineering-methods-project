package nl.tudelft.sem.template.example.domain.event;

import javax.persistence.AttributeConverter;

public class IsCancelledAttributeConverter implements AttributeConverter<IsCancelled, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(IsCancelled attribute) {
        return attribute.getCancelStatus();
    }

    @Override
    public IsCancelled convertToEntityAttribute(Boolean dbData) {
        return new IsCancelled(dbData);
    }
}
