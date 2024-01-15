package nl.tudelft.sem.template.domain.user;

import javax.persistence.AttributeConverter;

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
