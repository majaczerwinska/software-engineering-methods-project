package nl.tudelft.sem.template.example.domain.attendee;

import lombok.Getter;

@Getter
public class Role {

    private final transient RoleTitle roleTitle;

    public Role(RoleTitle role) {
        this.roleTitle = role;
    }

}
