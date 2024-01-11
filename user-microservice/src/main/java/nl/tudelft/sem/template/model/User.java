package nl.tudelft.sem.template.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-11T20:41:13.364276600+01:00[Europe/Berlin]")
public class User {

  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  private String affiliation;

  private String personalWebsite;

  private String preferredCommunication;

  public User id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
  */
  
  @Schema(name = "firstName", example = "Marieke", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public User lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
  */
  
  @Schema(name = "lastName", example = "Smith", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  */
  
  @Schema(name = "email", example = "pete@email.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User affiliation(String affiliation) {
    this.affiliation = affiliation;
    return this;
  }

  /**
   * Get affiliation
   * @return affiliation
  */
  
  @Schema(name = "affiliation", example = "Fireman", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affiliation")
  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public User personalWebsite(String personalWebsite) {
    this.personalWebsite = personalWebsite;
    return this;
  }

  /**
   * Get personalWebsite
   * @return personalWebsite
  */
  
  @Schema(name = "personalWebsite", example = "myPersonalWebsite.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personalWebsite")
  public String getPersonalWebsite() {
    return personalWebsite;
  }

  public void setPersonalWebsite(String personalWebsite) {
    this.personalWebsite = personalWebsite;
  }

  public User preferredCommunication(String preferredCommunication) {
    this.preferredCommunication = preferredCommunication;
    return this;
  }

  /**
   * Get preferredCommunication
   * @return preferredCommunication
  */
  
  @Schema(name = "preferredCommunication", example = "e-mail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredCommunication")
  public String getPreferredCommunication() {
    return preferredCommunication;
  }

  public void setPreferredCommunication(String preferredCommunication) {
    this.preferredCommunication = preferredCommunication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.affiliation, user.affiliation) &&
        Objects.equals(this.personalWebsite, user.personalWebsite) &&
        Objects.equals(this.preferredCommunication, user.preferredCommunication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, affiliation, personalWebsite, preferredCommunication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    affiliation: ").append(toIndentedString(affiliation)).append("\n");
    sb.append("    personalWebsite: ").append(toIndentedString(personalWebsite)).append("\n");
    sb.append("    preferredCommunication: ").append(toIndentedString(preferredCommunication)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

