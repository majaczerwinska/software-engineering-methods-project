package nl.tudelft.sem.template.example.domain.attendee;

import javax.persistence.AttributeConverter;

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
