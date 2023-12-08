package nl.tudelft.sem.template.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

/**
 * Track
 */

@Entity
public class Track {

  @Id
  @Column(nullable = false, unique = true)
  private Long id;

  @Column(nullable = false, unique = false)
  private String title;

  @Column(nullable = false, unique = false)
  private String description;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false, unique = false)
  private PaperType paperType;

  @Column(nullable = false, unique = false)
  private String submitDeadline;

  @Column(nullable = false, unique = false)
  private String reviewDeadline;

  @Column(nullable = false, unique = false)
  private Long eventId;

}

