package nl.tudelft.sem.template.domain.user;

public class FirstName {
    private final transient String name;

    public FirstName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
