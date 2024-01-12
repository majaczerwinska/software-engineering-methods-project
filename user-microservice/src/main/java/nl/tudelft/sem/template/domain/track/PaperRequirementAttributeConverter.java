package nl.tudelft.sem.template.domain.track;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nl.tudelft.sem.template.model.PaperType;


/**
 * JPA Converter for the paper requirement value object.
 */
@Converter
public class PaperRequirementAttributeConverter
        implements AttributeConverter<PaperRequirement, String> {

    @Override
    public String convertToDatabaseColumn(PaperRequirement attribute) {
        return attribute.toPaperType().name();
    }

    @Override
    public PaperRequirement convertToEntityAttribute(String dbData) {
        return new PaperRequirement(PaperType.valueOf(dbData));
    }

}
