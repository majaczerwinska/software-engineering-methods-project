package nl.tudelft.sem.template.domain.attendee;

import lombok.Getter;
import nl.tudelft.sem.template.enums.RoleTitle;

@Getter
public class Role {

    private final transient RoleTitle roleTitle;

    public Role(RoleTitle role) {
        this.roleTitle = role;
    }

}
