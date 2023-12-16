package nl.tudelft.sem.template.example.domain.attendee;

public enum RoleTitle {
    GeneralChair("General_Chair", 0, 0),
    PCChair("PC_Chair", 1, 1),
    PCMember("PC_Member", 2, 3),
    SubReviewer("Sub-Reviewer", 3, 4),
    Author("Author", 4, 4),
    Attendee("Attendee", 4, 4);


    //Title Name
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
