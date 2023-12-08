package nl.tudelft.sem.template.domain;

import org.openapitools.jackson.nullable.JsonNullable;

import javax.persistence.*;

/**
 * Attendee
 */

@Entity
public class Role {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long eventId;

    @Column
    private JsonNullable<Long> trackId = JsonNullable.undefined();

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private RoleTitle role;

    @Column(nullable = false)
    private Boolean confirmed;
}

