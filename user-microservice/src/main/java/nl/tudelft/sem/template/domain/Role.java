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

  @Column(nullable = false, unique = false)
  private Long userId;

  @Column(nullable = false, unique = false)
  private Long eventId;

  @Column(nullable = true, unique = false)
  private JsonNullable<Long> trackId = JsonNullable.undefined();

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false, unique = false)
  private RoleTitle role;

  @Column(nullable = false, unique = false)
  private Boolean confirmed;
}

