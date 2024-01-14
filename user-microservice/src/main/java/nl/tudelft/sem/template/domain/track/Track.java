package nl.tudelft.sem.template.domain.track;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import java.time.LocalDate;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.domain.HasEvents;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.events.TrackCreatedEvent;
import nl.tudelft.sem.template.events.TrackDeadlineChangedEvent;
import nl.tudelft.sem.template.events.TrackDescriptionChangedEvent;
import nl.tudelft.sem.template.events.TrackPaperRequirementChangedEvent;
import nl.tudelft.sem.template.events.TrackParentEventChangedEvent;
import nl.tudelft.sem.template.events.TrackRemovedEvent;
import nl.tudelft.sem.template.events.TrackTitleChangedEvent;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

/**
 * A DDD entity representing a track in our domain.
 */
@Entity
@Table(name = "tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Track extends HasEvents {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Convert(converter = TitleAttributeConverter.class)
    private Title title;

    @Column(name = "description", nullable = false)
    @Convert(converter = DescriptionAttributeConverter.class)
    private Description description;

    @Column(name = "paperType", nullable = false)
    @Convert(converter = PaperRequirementAttributeConverter.class)
    private PaperRequirement paperType;

    @Column(name = "submitDeadline", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate submitDeadline;

    @Column(name = "reviewDeadline", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate reviewDeadline;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Event event;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees;

    public Track(Long id) {
        this.id = id;
    }

    /**
     * a constructor for Track.
     * where the deadlines can be inputted as the latest time,
     * if the user does not specify.
     *
     * @param title          the title of this track
     * @param description    the detailed info about this track
     * @param paperType      the allowed paper type for submission for this track
     * @param submitDeadline the deadline for submission in this track
     * @param reviewDeadline the deadline for giving reviews in this track
     * @param event  the event this track belongs to
     */
    public Track(Title title, Description description, PaperRequirement paperType,
            LocalDate submitDeadline, LocalDate reviewDeadline, Event event) {
        this.title = title;
        this.description = description;
        this.paperType = paperType;
        this.submitDeadline = submitDeadline;
        this.reviewDeadline = reviewDeadline;
        this.event = event;
        this.recordThat(new TrackCreatedEvent(event.getId(), this.id));
    }

    /**
     * A constructor with Id.
     */
    public Track(Long id, Title title, Description description, PaperRequirement paperType,
            LocalDate submitDeadline, LocalDate reviewDeadline, Event event) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.paperType = paperType;
        this.submitDeadline = submitDeadline;
        this.reviewDeadline = reviewDeadline;
        this.event = event;
        this.recordThat(new TrackCreatedEvent(event.getId(), this.id));
    }

    /**
     * method for changing the id of this track.
     *
     * @param id to set id for this track
     */
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * method for changing the id of this track.
     *
     * @param title for this track
     */
    public void setTitle(Title title) {
        this.title = title;
        this.recordThat(new TrackTitleChangedEvent(this));
    }

    /**
     * method for changing the description of this track.
     *
     * @param description for this track
     */
    public void setDescription(Description description) {
        this.description = description;
        this.recordThat(new TrackDescriptionChangedEvent(this));
    }

    /**
     * method for changing the paper type of this track.
     *
     * @param paperType that is allowed for this track
     */
    public void setPaperType(PaperRequirement paperType) {
        this.paperType = paperType;
        this.recordThat(new TrackPaperRequirementChangedEvent(this));
    }

    /**
     * method for changing the deadline for submission of this track.
     *
     * @param submitDeadline for this track
     */
    public void setSubmitDeadline(LocalDate submitDeadline) {
        this.submitDeadline = submitDeadline;
        this.recordThat(new TrackDeadlineChangedEvent(this));
    }

    /**
     * method for changing the deadline for reviewing of this track.
     *
     * @param reviewDeadline for this track
     */
    public void setReviewDeadline(LocalDate reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
        this.recordThat(new TrackDeadlineChangedEvent(this));
    }

    /**
     * method for change the event of this track.
     *
     * @param event Event which this track belongs to
     */
    public void setEvent(Event event) {
        Event temp = this.event;
        this.event = event;
        this.recordThat(new TrackRemovedEvent(temp.getId(), this.id));
        this.recordThat(new TrackParentEventChangedEvent(this));
        this.recordThat(new TrackCreatedEvent(event.getId(), this.id));
    }

    /**
     * method check the quality between this track and a unknown object.
     *
     * @param o the object where this will be compared to
     * @return the result of the test for equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Track)) {
            return false;
        }
        Track track = (Track) o;
        return title.equals(track.title) && event.getId().equals(track.event.getId());
    }

    /**
     * method to generate a unique int for this entity.
     *
     * @return a unique int for this entity and the hashcode will be stored as the
     *         id of this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, event.getId());
    }

    /**
     * e.g.
     * * Track"memory address of this task"[
     * * id= 1
     * * title = TrackName;
     * * description = TrackInfo;
     * * paperType = FULL_PAPER;
     * * ...
     * * ]
     *
     * @return in the MULTI_LINE_STYLE
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /**
     * a converter for Track. form domain to model
     *
     * @return track in domain Track format
     */
    public nl.tudelft.sem.template.model.Track toModelTrack() {
        nl.tudelft.sem.template.model.Track track = new nl.tudelft.sem.template.model.Track();
        track.setTitle(this.title.toString());
        track.setDescription(this.description.toString());
        track.setPaperType(this.paperType.toPaperType());
        track.setSubmitDeadline(this.submitDeadline);
        track.setReviewDeadline(this.reviewDeadline);
        track.setEventId(this.event.getId());
        return track;
    }
}
