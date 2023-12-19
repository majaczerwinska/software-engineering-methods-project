package nl.tudelft.sem.template.domain.track;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the paper requirement value object.
 */
@Converter
public class PaperRequirementAttributeConverter
        implements AttributeConverter<PaperRequirement, PaperType> {

    @Override
    public PaperType convertToDatabaseColumn(PaperRequirement attribute) {
        return attribute.toPaperType();
    }

    @Override
    public PaperRequirement convertToEntityAttribute(PaperType dbData) {
        return new PaperRequirement(dbData);
    }

}
