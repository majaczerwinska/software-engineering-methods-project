package nl.tudelft.sem.template.domain.track;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/**
 * JPA Converter for the title value object.
 */
@Converter
public class TitleAttributeConverter implements AttributeConverter<Title, String> {

    @Override
    public String convertToDatabaseColumn(Title attribute) {
        return attribute.toString();
    }

    @Override
    public Title convertToEntityAttribute(String dbData) {
        return new Title(dbData);
    }

}