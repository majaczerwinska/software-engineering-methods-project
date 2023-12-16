package nl.tudelft.sem.template.example.domain.attendee;

import javax.persistence.AttributeConverter;

public class RoleAttributeConverter implements AttributeConverter<Role, RoleTitle> {
    @Override
    public RoleTitle convertToDatabaseColumn(Role attribute) {
        return attribute.getRoleTitle();
    }

    @Override
    public Role convertToEntityAttribute(RoleTitle dbData) {
        return new Role(dbData);
    }
}
