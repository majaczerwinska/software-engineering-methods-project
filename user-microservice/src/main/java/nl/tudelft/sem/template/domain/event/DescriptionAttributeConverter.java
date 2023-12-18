package nl.tudelft.sem.template.domain.event;

import javax.persistence.AttributeConverter;

public class DescriptionAttributeConverter implements AttributeConverter<Description, String> {

    @Override
    public String convertToDatabaseColumn(Description attribute) {
        return attribute.toString();
    }

    @Override
    public Description convertToEntityAttribute(String dbData) {
        return new Description(dbData);
    }
}
