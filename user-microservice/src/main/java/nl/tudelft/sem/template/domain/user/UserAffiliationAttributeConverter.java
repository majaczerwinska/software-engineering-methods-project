package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

/**
 * JPA Converter for the UserAffiliation value object.
 */
public class UserAffiliationAttributeConverter implements AttributeConverter<UserAffiliation, String> {


    @Override
    public String convertToDatabaseColumn(UserAffiliation attribute) {
        return attribute.toString();
    }

    @Override
    public UserAffiliation convertToEntityAttribute(String dbData) {
        return new UserAffiliation(dbData);
    }
}
