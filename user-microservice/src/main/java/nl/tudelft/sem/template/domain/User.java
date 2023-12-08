package nl.tudelft.sem.template.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * User
 */

@Entity
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String affiliation;

    @Column(nullable = false)
    private String personalWebsite;

    @Column(nullable = false)
    private String preferredCommunication;

}

