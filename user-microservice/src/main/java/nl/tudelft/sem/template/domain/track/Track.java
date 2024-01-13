package nl.tudelft.sem.template.domain.track;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.domain.HasEvents;
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

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitDeadline", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate submitDeadline;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reviewDeadline", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate reviewDeadline;

    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    //@JoinColumn(name = "eventId", referencedColumnName = "id")
    //@JsonBackReference
    //@Column(name = "event", nullable = false)
    //@Convert(converter = ParentEventAttributeConverter.class)
    //From Yair: The converter is not correct. How can it serialize an event instance?
    @Column(name = "parentEventId", nullable = false)
    private Long parentEventId;

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
     * @param parentEventId  the event this track belongs to
     */
    public Track(Title title, Description description, PaperRequirement paperType,
                 LocalDate submitDeadline, LocalDate reviewDeadline, Long parentEventId) {
        this.title = title;
        this.description = description;
        this.paperType = paperType;
        this.submitDeadline = submitDeadline;
        this.reviewDeadline = reviewDeadline;
        this.parentEventId = parentEventId;
        this.recordThat(new TrackCreatedEvent(parentEventId, this.id));
    }


    /**
     * a converter for Track. form model to domain
     *
     * @param track the track in model format
     */
    public Track(nl.tudelft.sem.template.model.Track track) {
        this.title = new Title(track.getTitle());
        this.description = new Description(track.getDescription());
        this.paperType = new PaperRequirement(track.getPaperType());
        this.submitDeadline = LocalDate.parse(track.getSubmitDeadline());
        this.reviewDeadline = LocalDate.parse(track.getReviewDeadline());
        this.parentEventId = track.getEventId();
        this.recordThat(new TrackCreatedEvent(parentEventId, this.id));
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
     * @param parentEventId which this track belongs to
     */
    public void setParentEventId(Long parentEventId) {
        Long temp = this.parentEventId;
        this.parentEventId = parentEventId;
        this.recordThat(new TrackRemovedEvent(temp, this.id));
        this.recordThat(new TrackParentEventChangedEvent(this));
        this.recordThat(new TrackCreatedEvent(parentEventId, this.id));
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
                && Objects.equals(parentEventId, track.parentEventId);
    }

    /** The hashcode will be stored as the id of this entity.
     *
     * @return a unique int for this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, paperType, parentEventId);
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
        track.setEventId(this.parentEventId);
        return track;
    }
}