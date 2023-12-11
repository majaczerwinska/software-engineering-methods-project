package nl.tudelft.sem.template.example.domain.user;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    @Column(name = "password_hash", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @Column(name = "username", nullable = false)
    private String name;

    @Column(name = "affiliation")
    private String affiliation;

    @Column(name = "link")
    private String link;

    @Column(name = "communication")
    private String communication;

    //    @ManyToMany
    //    @JoinTable(
    //            name = "user_event",
    //            joinColumns = @JoinColumn(name = "user_id"),
    //            inverseJoinColumns = @JoinColumn(name = "event_id")
    //    )
    //    private Set<Event> events = new HashSet<>();



    /**
     * Create new application user.
     *
     * @param email The Email for the new user
     * @param password The password for the new user
     */
    public AppUser(Email email, HashedPassword password,
                   String name, String affiliation, String link, String communication) {
        this.email = email;
        this.password = password;
        this.recordThat(new UserWasCreatedEvent(email));
        this.name = name;
        this.affiliation = affiliation;
        this.link = link;
        this.communication = communication;
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return id == (appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
