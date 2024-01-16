package nl.tudelft.sem.template.domain.user;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
import nl.tudelft.sem.template.enums.LogType;
import nl.tudelft.sem.template.logs.LogFactory;
import nl.tudelft.sem.template.logs.event.EventLogFactory;
import nl.tudelft.sem.template.logs.user.UserLog;
import nl.tudelft.sem.template.logs.user.UserLogFactory;
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
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * 3-argument constructor.
     *
     * @param email email
     * @param firstName firstName
     * @param lastName lastName
     */
    public AppUser(Email email, Name firstName, Name lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
        if (user == null || !user.getEmail().contains("@")) {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setFirstName(Name firstName) {
        this.firstName = firstName;
    }

    public void setLastName(Name lastName) {
        this.lastName = lastName;
    }

    public void setAffiliation(UserAffiliation affiliation) {
        this.affiliation = affiliation;
        ((UserLogFactory) LogFactory.loadFactory(LogType.USER)).registerUserAffiliationChange(this);
    }

    public void setLink(Link link) {
        this.link = link;
        ((UserLogFactory) LogFactory.loadFactory(LogType.USER)).registerUserLinkChange(this);
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
        ((UserLogFactory) LogFactory.loadFactory(LogType.USER)).registerUserCommunicationChange(this);
    }

    public void setAttendance(List<Attendee> attendance) {
        this.attendance = attendance;
        ((UserLogFactory) LogFactory.loadFactory(LogType.USER)).registerUserAttendanceChange(this);
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
        return Objects.equals(id, appUser.id);
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
