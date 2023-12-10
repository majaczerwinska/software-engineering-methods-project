package nl.tudelft.sem.template.example.domain.event;

import javax.persistence.AttributeConverter;

public class IsCancelledAttributeConverter implements AttributeConverter<IsCancelled, Integer> {
    @Override
    public Integer convertToDatabaseColumn(IsCancelled attribute) {
        return attribute.getCancelStatus();
    }

    @Override
    public IsCancelled convertToEntityAttribute(Integer dbData) {
        return new IsCancelled(dbData);
    }
}
