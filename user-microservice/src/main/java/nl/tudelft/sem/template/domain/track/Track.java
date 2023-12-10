package nl.tudelft.sem.template.domain.track;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.domain.Event;
import nl.tudelft.sem.template.domain.HasEvents;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * A DDD entity representing a track in our domain.
 */
@Entity
@Table(name = "tracks")
@NoArgsConstructor
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "paperType", nullable = false)
    @Convert(converter = PaperRequirementAttributeConverter.class)
    private PaperRequirement paperType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitDeadline", nullable = false)
    @Convert(converter = DeadlineAttributeConverter.class)
    private Deadline submitDeadline;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reviewDeadline", nullable = false)
    @Convert(converter = DeadlineAttributeConverter.class)
    private Deadline reviewDeadline;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "eventId", referencedColumnName = "id")
    @JsonBackReference
    @Column(name = "event", nullable = false)
    @Convert(converter = ParentEventAttributeConverter.class)
    private ParentEvent event;

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
     * @param event          the event this track belongs to
     */
    public Track(Title title, Description description, PaperRequirement paperType,
                 Deadline submitDeadline, Deadline reviewDeadline, ParentEvent event) {
        this.title = title;
        this.description = description;
        this.paperType = paperType;
        this.submitDeadline = submitDeadline;
        this.reviewDeadline = reviewDeadline;
        this.event = event;
        this.recordThat(new TrackWasCreatedEvent(event.toEvent(), this.id));
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
        this.recordThat(new TitleWasChangedEvent(this));
    }


    /**
     * method for changing the description of this track.
     *
     * @param description for this track
     */
    public void setDescription(Description description) {
        this.description = description;
        this.recordThat(new DescriptionWasChangedEvent(this));
    }

    /**
     * method for changing the paper type of this track.
     *
     * @param paperType that is allowed for this track
     */
    public void setPaperType(PaperRequirement paperType) {
        this.paperType = paperType;
        this.recordThat(new PaperRequirementWasChangedEvent(this));
    }


    /**
     * method for changing the deadline for submission of this track.
     *
     * @param submitDeadline for this track
     */
    public void setSubmitDeadline(Deadline submitDeadline) {
        this.submitDeadline = submitDeadline;
        this.recordThat(new DeadlineWasChangedEvent(this));
    }

    /**
     * method for changing the deadline for reviewing of this track.
     *
     * @param reviewDeadline for this track
     */
    public void setReviewDeadline(Deadline reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
        this.recordThat(new DeadlineWasChangedEvent(this));
    }


    /**
     * method for change the event of this track.
     *
     * @param event which this track belongs to
     */
    public void setEvent(ParentEvent event) {
        Event temp = this.event.toEvent();
        this.event = event;
        this.recordThat(new TrackWasRemovedEvent(temp, this.id));
        this.recordThat(new ParentEventWasChangedEvent(this));
        this.recordThat(new TrackWasCreatedEvent(event.toEvent(), this.id));
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
        return title.equals(track.title) && event.equals(track.event);
    }

    /**
     * mothod to generate a unique int for this entity.
     *
     * @return a unique int for this entity and the hashcode will be stored as the id of this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, event);
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
}