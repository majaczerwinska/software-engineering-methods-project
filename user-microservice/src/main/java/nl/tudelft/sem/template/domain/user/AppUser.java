package nl.tudelft.sem.template.domain.user;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.domain.HasEvents;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.user.converters.EmailAttributeConverter;
import nl.tudelft.sem.template.model.User;


/**
 * A DDD entity representing an application user in our domain.
 */


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    @NonNull
    private Email email;

    @Column(name = "firstName", nullable = false)
    @Convert(converter = NameAttributeConverter.class)
    private Name firstName;

    @Column(name = "lastName", nullable = false)
    @Convert(converter = NameAttributeConverter.class)
    private Name lastName;

    @Column(name = "affiliation", nullable = true)
    @Convert(converter = UserAffiliationAttributeConverter.class)
    private UserAffiliation affiliation;

    @Column(name = "link", nullable = true)
    @Convert(converter = LinkAttributeConverter.class)
    private Link link;

    @Column(name = "communication", nullable = true)
    @Convert(converter = CommunicationAttributeConverter.class)
    private Communication communication;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendance;

    public AppUser(Long id) {
        this.id = id;
    }

    public AppUser(Email email, Name firstName) {
        this.email = email;
        this.firstName = firstName;
    }

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
        this.firstName = firstName;
        this.lastName = lastName;
        this.affiliation = affiliation;
        this.link = link;
        this.communication = communication;
    }

    /**
     * Constructor for converting model User into AppUser.
     *
     * @param user model User to convert into AppUser
     */
    public AppUser(User user) {
        if (user == null || user.getId() < 0 || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid user data");
        }
        this.id = user.getId();
        this.email = new Email(user.getEmail());
        this.firstName = new Name(user.getFirstName());
        this.lastName = new Name(user.getLastName());
        this.affiliation = new UserAffiliation(user.getAffiliation());
        this.link = new Link(user.getPersonalWebsite());
        this.communication = new Communication(user.getPreferredCommunication());
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

    /**
     * Extends the accessor to a public visibility.
     *
     * @param object The log to be recorded.
     */
    public void recordLog(Object object) {
        this.recordThat(object);
    }
}
