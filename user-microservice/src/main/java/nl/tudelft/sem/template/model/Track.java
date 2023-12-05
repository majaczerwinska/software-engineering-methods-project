package nl.tudelft.sem.template.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import nl.tudelft.sem.template.model.PaperType;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Track
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-12-05T18:45:04.605384300+01:00[Europe/Amsterdam]")
public class Track {

  private Long id;

  private String title;

  private String description;

  private String submitDeadline;

  private String reviewDeadline;

  private PaperType paperType;

  private Long eventId;

  public Track id(Long id) {
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

  public Track title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  */
  
  @Schema(name = "title", example = "track Title 1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Track description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  
  @Schema(name = "description", example = "track 1 is ..., aims to ...", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Track submitDeadline(String submitDeadline) {
    this.submitDeadline = submitDeadline;
    return this;
  }

  /**
   * the deadline for all paper submissions
   * @return submitDeadline
  */
  
  @Schema(name = "submit_deadline", example = "2022/11/24, 21:59", description = "the deadline for all paper submissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("submit_deadline")
  public String getSubmitDeadline() {
    return submitDeadline;
  }

  public void setSubmitDeadline(String submitDeadline) {
    this.submitDeadline = submitDeadline;
  }

  public Track reviewDeadline(String reviewDeadline) {
    this.reviewDeadline = reviewDeadline;
    return this;
  }

  /**
   * the deadline for all paper reviews
   * @return reviewDeadline
  */
  
  @Schema(name = "review_deadline", example = "2022/11/24, 21:59", description = "the deadline for all paper reviews", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("review_deadline")
  public String getReviewDeadline() {
    return reviewDeadline;
  }

  public void setReviewDeadline(String reviewDeadline) {
    this.reviewDeadline = reviewDeadline;
  }

  public Track paperType(PaperType paperType) {
    this.paperType = paperType;
    return this;
  }

  /**
   * Get paperType
   * @return paperType
  */
  @Valid 
  @Schema(name = "paper_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("paper_type")
  public PaperType getPaperType() {
    return paperType;
  }

  public void setPaperType(PaperType paperType) {
    this.paperType = paperType;
  }

  public Track eventId(Long eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
  */
  
  @Schema(name = "event_id", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Track track = (Track) o;
    return Objects.equals(this.id, track.id) &&
        Objects.equals(this.title, track.title) &&
        Objects.equals(this.description, track.description) &&
        Objects.equals(this.submitDeadline, track.submitDeadline) &&
        Objects.equals(this.reviewDeadline, track.reviewDeadline) &&
        Objects.equals(this.paperType, track.paperType) &&
        Objects.equals(this.eventId, track.eventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, submitDeadline, reviewDeadline, paperType, eventId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Track {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    submitDeadline: ").append(toIndentedString(submitDeadline)).append("\n");
    sb.append("    reviewDeadline: ").append(toIndentedString(reviewDeadline)).append("\n");
    sb.append("    paperType: ").append(toIndentedString(paperType)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
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

