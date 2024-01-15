package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.AttributeConverter;


public class UserAffiliationAttributeConverter implements AttributeConverter<UserAffiliation, String> {
    @Override
    public String convertToDatabaseColumn(UserAffiliation attribute) {
        return Objects.toString(attribute);
    }

    @Override
    public UserAffiliation convertToEntityAttribute(String dbData) {
        return new UserAffiliation(dbData);
    }

}
