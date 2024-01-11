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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    private Email email;

    @Column(name = "firstName", nullable = false)
    @Convert(converter = FirstNameAttributeConverter.class)
    private FirstName firstName;

    @Column(name = "lastName", nullable = false)
    @Convert(converter = LastNameAttributeConverter.class)
    private LastName lastName;

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
     * Create new application user.
     *
     * @param email    The Email for the new user
     */
    public AppUser(Long id, Email email, FirstName firstName, LastName lastName,
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

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FirstName getFirstName() {
        return firstName;
    }

    public void setFirstName(FirstName firstName) {
        this.firstName = firstName;
    }

    public LastName getLastName() {
        return lastName;
    }

    public void setLastName(LastName lastName) {
        this.lastName = lastName;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
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
        AppUser that = (AppUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * A converter for User class, from domain to model.
     *
     * @return user from model class
     */
    public User toModelUser() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName.toString());
        user.setLastName(this.lastName.toString());
        user.setEmail(this.email.toString());
        user.setAffiliation(this.affiliation.toString());
        user.setPersonalWebsite(this.link.toString());
        user.setPreferredCommunication(this.communication.toString());
        return user;
    }
}
