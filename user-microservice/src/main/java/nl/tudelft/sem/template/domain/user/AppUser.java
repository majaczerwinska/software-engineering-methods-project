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

    @Column(name = "username", nullable = false)
    private UserName name;

    @Column(name = "affiliation")
    private Affiliation affiliation;

    @Column(name = "link")
    private UserLink link;

    @Column(name = "communication")
    private UserCommunication communication;

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
                   UserName name, Affiliation affiliation, UserLink link, UserCommunication communication) {
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

    public void setCommunication(UserCommunication communication) {
        this.communication = communication;
    }

    public void setName(UserName name) {
        this.name = name;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setLink(UserLink link) {
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
