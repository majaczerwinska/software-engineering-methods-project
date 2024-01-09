package nl.tudelft.sem.template.domain.user.converters;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.domain.user.UserAffiliation;

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
