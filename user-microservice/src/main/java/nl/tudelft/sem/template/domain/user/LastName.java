package nl.tudelft.sem.template.domain.user;

public class LastName {
    private final transient String name;

    public LastName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
