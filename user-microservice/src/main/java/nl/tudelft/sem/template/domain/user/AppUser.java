package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.domain.HasEvents;


/**
 * A DDD entity representing an application user in our domain.
 */
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    @Column(name = "name", nullable = false)
    private Name name;

    @Column(name = "affiliation")
    private UserAffiliation affiliation;

    @Column(name = "link")
    private Link link;

    @Column(name = "communication")
    private Communication communication;

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
     * @param email    The Email for the new user
     */
    public AppUser(Email email,
                   Name name, UserAffiliation affiliation, Link link, Communication communication) {
        this.email = email;
        this.recordThat(new UserWasCreatedEvent(email));
        this.name = name;
        this.affiliation = affiliation;
        this.link = link;
        this.communication = communication;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    private void setId(int id) {
        this.id = id;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setAffiliation(UserAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setLink(Link link) {
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
