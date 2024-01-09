package nl.tudelft.sem.template.domain.attendee;

import lombok.Getter;
import nl.tudelft.sem.template.enums.RoleTitle;

/**
 * A DDD value object representing the role the attendee has in our domain.
 */
@Getter
public class Role {

    private final transient RoleTitle roleTitle;

    public Role(RoleTitle role) {
        this.roleTitle = role;
    }

}
