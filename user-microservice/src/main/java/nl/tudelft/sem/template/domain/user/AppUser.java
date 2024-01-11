package nl.tudelft.sem.template.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.domain.HasEvents;
import nl.tudelft.sem.template.domain.user.converters.EmailAttributeConverter;
import nl.tudelft.sem.template.events.UserWasCreatedEvent;
import nl.tudelft.sem.template.model.User;


/**
 * A DDD entity representing an application user in our domain.
 */


@Entity
@Table(name = "users")
@Getter
@Setter
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

    @Column(name = "firstName", nullable = false)
    @Convert(converter = NameAttributeConverter.class)
    private Name firstName;

    @Column(name = "lastName", nullable = false)
    @Convert(converter = NameAttributeConverter.class)
    private Name lastName;

    @Column(name = "affiliation")
    @Convert(converter =  UserAffiliationAttributeConverter.class)
    private UserAffiliation affiliation;

    @Column(name = "link")
    @Convert(converter = LinkAttributeConverter.class)
    private Link link;

    @Column(name = "communication")
    @Convert(converter = CommunicationAttributeConverter.class)
    private Communication communication;

    //    @ManyToMany
    //    @JoinTable(
    //            name = "user_event",
    //            joinColumns = @JoinColumn(name = "user_id"),
    //            inverseJoinColumns = @JoinColumn(name = "event_id")
    //    )
    //    private Set<Event> events = new HashSet<>();


    /**
     * Constructor.
     *
     * @param email - email
     * @param firstName - first name
     * @param lastName - last name
     * @param affiliation - affiliation
     * @param link - link
     * @param communication - communication
     */
    public AppUser(Email email, Name firstName, Name lastName,
                   UserAffiliation affiliation, Link link, Communication communication) {
        this.email = email;
        this.recordThat(new UserWasCreatedEvent(email));
        this.firstName = firstName;
        this.lastName = lastName;
        this.affiliation = affiliation;
        this.link = link;
        this.communication = communication;
    }

    /**
     * Constructor for testing.
     *
     * @param id - id
     * @param email - email
     * @param firstName - first name
     * @param lastName - last name
     * @param affiliation - affiliation
     * @param link - link
     * @param communication - communication
     */
    public AppUser(long id, Email email, Name firstName, Name lastName,
                   UserAffiliation affiliation, Link link, Communication communication) {
        this.id = id;
        this.email = email;
        this.recordThat(new UserWasCreatedEvent(email));
        this.firstName = firstName;
        this.lastName = lastName;
        this.affiliation = affiliation;
        this.link = link;
        this.communication = communication;
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
        return Objects.hash(id) + Objects.hash(email) + Objects.hash(firstName);
    }

    /**
     * Converts the AppUser into a User.
     *
     * @return - the model user
     */
    public User toModelUser() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName.toString());
        user.setLastName(this.lastName.toString());
        user.setPreferredCommunication(this.communication.toString());
        user.setPersonalWebsite(this.link.toString());
        user.setAffiliation(this.affiliation.toString());
        user.setEmail(this.email.toString());
        return user;
    }
}
