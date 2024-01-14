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
    private long id;

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
    }

    /**
     * method for changing the id of this track.
     *
     * @param id for this track
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * method for changing the title of this track.
     *
     * @param title for this track
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     * method for changing the description of this track.
     *
     * @param description for this track
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * method for changing the paper type of this track.
     *
     * @param paperType that is allowed for this track
     */
    public void setPaperType(PaperRequirement paperType) {
        this.paperType = paperType;
    }

    /**
     * method for changing the deadline for submission of this track.
     *
     * @param submitDeadline for this track
     */
    public void setSubmitDeadline(LocalDate submitDeadline) {
        this.submitDeadline = submitDeadline;
    }

    /**
     * method for changing the deadline for reviewing of this track.
     *
     * @param reviewDeadline for this track
     */
    public void setReviewDeadline(LocalDate reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }

    /**
     * method for change the event of this track.
     *
     * @param event Event which this track belongs to
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /** the equals function for track.
     *
     * @param o the object to compare to
     * @return ture if they are the same
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
        if (title == null) {
            title = new Title(null);
        }
        if (track.title == null) {
            track.setTitle(new Title(null));
        }
        return Objects.equals(title.toString(), track.title.toString())
                && Objects.equals(paperType.toPaperType(), track.paperType.toPaperType())
                && Objects.equals(event, track.getEvent());
    }

    /** The hashcode will be stored as the id of this entity.
     *
     * @return a unique int for this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, paperType, event.getId());
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
        track.setSubmitDeadline(this.submitDeadline.toString());
        track.setReviewDeadline(this.reviewDeadline.toString());
        track.setEventId(this.event.getId());
        return track;
    }

    /**
     * Extends the accessor to a public visibility.
     *
     * @param object The log to be recorded.
     */
    public void recordLog(Object object) {
        this.recordThat(object);
    }
}