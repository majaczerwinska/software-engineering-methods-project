package nl.tudelft.sem.template.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * Event
 */

@Entity
public class Event {

  @Id
  @Column(nullable = false, unique = true)
  private Long id;

  @Column(nullable = false, unique = false)
  private String title;

  @Column(nullable = true, unique = false)
  private JsonNullable<String> description = JsonNullable.undefined();

  @Column(nullable = false, unique = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @Column(nullable = false, unique = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  @Column(nullable = false, unique = false)
  private Boolean isCancelled;
}

