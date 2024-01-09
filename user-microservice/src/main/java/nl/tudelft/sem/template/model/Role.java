package nl.tudelft.sem.template.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Role
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-09T20:30:54.240162642+01:00[Europe/Amsterdam]")
public enum Role {
  
  GENERAL_CHAIR("general_chair"),
  
  PC_CHAIR("pc_chair"),
  
  PC_MEMBER("pc_member"),
  
  SUB_REVIEWER("sub_reviewer"),
  
  AUTHOR("author"),
  
  ATTENDEE("attendee");

  private String value;

  Role(String value) {
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
  public static Role fromValue(String value) {
    for (Role b : Role.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

