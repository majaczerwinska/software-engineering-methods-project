package nl.tudelft.sem.template.domain.user.attributeConverters;

import nl.tudelft.sem.template.domain.user.UserAffiliation;
import nl.tudelft.sem.template.model.User;

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
