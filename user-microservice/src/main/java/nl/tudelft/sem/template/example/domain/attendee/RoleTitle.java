package nl.tudelft.sem.template.example.domain.attendee;

public enum RoleTitle {
    GENERAL_CHAIR("General Chair", 0, 0),
    PC_CHAIR("PC Chair", 1, 1),
    PC_MEMBER("PC Member", 2, 3),
    SUB_REVIEWER("Sub Reviewer", 3, 4),
    AUTHOR("Author", 4, 4),
    ATTENDEE("Attendee", 4, 4);


    // The fancy title of the role
    private final String title;
    // The level of permission required in order to affect this role
    private final int precedence;
    // The least precedence that can be affected by this role
    private final int permission;

    RoleTitle(String title, int precedence, int permission) {
        this.precedence = precedence;
        this.permission = permission;
        this.title = title;
    }

    private int getPrecedence() {
        return this.precedence;
    }

    private int getPermission() {
        return this.permission;
    }

    private String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
