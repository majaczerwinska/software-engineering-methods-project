package nl.tudelft.sem.template.domain.track;

import com.fasterxml.jackson.annotation.JsonBackReference;
import nl.tudelft.sem.template.domain.Event;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Table(name = "track")
public class Track {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "paperType", nullable = false)
    private PaperType paperType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitDeadline", nullable = false)
    private Date submitDeadline;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reviewDeadline", nullable = false)
    private Date reviewDeadline;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "eventId", referencedColumnName = "id")
    @JsonBackReference
    @Column(name = "event", nullable = false)
    private Event event;


    /**
     * empty Constructor: to avoid error
     */
    public Track() {
    }

    /**
     * @param title          the title of this track
     * @param description    the detailed info about this track
     * @param paperType      the allowed paper type for submission for this track
     * @param submitDeadline the deadline for submission in this track
     * @param reviewDeadline the deadline for giving reviews in this track
     * @param event          the event this track belongs to
     */
    public Track(String title, String description, PaperType paperType, Date submitDeadline, Date reviewDeadline, Event event) {
        this.title = title;
        this.description = description;
        this.paperType = paperType;
        this.submitDeadline = submitDeadline;
        this.reviewDeadline = reviewDeadline;
        this.event = event;
    }

    /**
     * @return the id of this track
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id to set id for this track
     */
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title for this track
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title for this track
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description for this track
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description for this track
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the allowed paper type for submission for this track
     */
    public PaperType getPaperType() {
        return paperType;
    }

    /**
     * @param paperType that is allowed for this track
     */
    public void setPaperType(PaperType paperType) {
        this.paperType = paperType;
    }

    /**
     * @return the deadline for submission for this track
     */
    public Date getSubmitDeadline() {
        return submitDeadline;
    }

    /**
     * @param submitDeadline for this track
     */
    public void setSubmitDeadline(Date submitDeadline) {
        this.submitDeadline = submitDeadline;
    }

    /**
     * @return the deadline for review for this track
     */
    public Date getReviewDeadline() {
        return reviewDeadline;
    }

    /**
     * @param reviewDeadline for this track
     */
    public void setReviewDeadline(Date reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }

    /**
     * @return the event this track belongs to
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event which this track belongs to
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @param o the object where this will be compared to
     * @return the result of the test for equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track)) return false;
        Track track = (Track) o;
        return title.equals(track.title) && event.equals(track.event);
    }

    /**
     * @return a unique int for this entity and the hashcode will be stored as the id of this entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, event);
    }

    /**
     * @return in the MULTI_LINE_STYLE, e.g.:
     * Track"memory address of this task"[
     * id= 1
     * title = TrackName;
     * description = TrackInfo;
     * paperType = FULL_PAPER;
     * ...
     * ]
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}