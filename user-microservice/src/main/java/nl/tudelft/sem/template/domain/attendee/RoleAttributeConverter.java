package nl.tudelft.sem.template.domain.attendee;

import javax.persistence.AttributeConverter;
import nl.tudelft.sem.template.enums.RoleTitle;

/**
 * JPA Converter for the Role value object.
 */
public class RoleAttributeConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.getRoleTitle().name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return new Role(RoleTitle.valueOf(dbData));
    }
}
