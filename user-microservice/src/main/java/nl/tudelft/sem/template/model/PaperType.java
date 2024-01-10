package nl.tudelft.sem.template.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets PaperType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-10T16:12:17.351545+01:00[Europe/Amsterdam]")
public enum PaperType {
  
  FULL_PAPER("full-paper"),
  
  SHORT_PAPER("short-paper"),
  
  POSITION_PAPER("position-paper");

  private String value;

  PaperType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PaperType fromValue(String value) {
    for (PaperType b : PaperType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

