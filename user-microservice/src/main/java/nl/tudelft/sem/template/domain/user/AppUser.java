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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = EmailAttributeConverter.class)
    @NonNull
    private Email email;

    @Column(name = "name", nullable = false)
    @Convert(converter = NameAttributeConverter.class)
    @NonNull
    private Name name;

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
     * Create new application user.
     *
     * @param email The Email for the new user
     */
    public AppUser(Email email,
            Name name, UserAffiliation affiliation, Link link, Communication communication) {
        this.email = email;
        this.name = name;
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
        return id.equals(appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
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
