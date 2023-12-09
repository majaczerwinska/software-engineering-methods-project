package nl.tudelft.sem.template.domain.track;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import nl.tudelft.sem.template.domain.Event;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * track can be created inside an event.
 */
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
     * empty Constructor: to avoid error.
     */
    public Track() {
    }

    /**
     * a constructor for Track.
     * where the deadlines can be inputted as the latest time,
     * if the user does not specify.
     *
     * @param t    the title of this track
     * @param d    the detailed info about this track
     * @param p    the allowed paper type for submission for this track
     * @param s    the deadline for submission in this track
     * @param r    the deadline for giving reviews in this track
     * @param e    the event this track belongs to
     */
    public Track(String t, String d, PaperType p, Date s, Date r, Event e) {
        this.title = t;
        this.description = d;
        this.paperType = p;
        this.submitDeadline = s;
        this.reviewDeadline = r;
        this.event = e;
    }

    /**
     * method for getting the id of this track.
     *
     * @return the id of this track.
     */
    public Long getId() {
        return id;
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
     * method for getting the title of this track.
     *
     * @return the title for this track
     */
    public String getTitle() {
        return title;
    }

    /**
     * method for changing the id of this track.
     *
     * @param title for this track
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * method for getting the description of this track.
     *
     * @return the description for this track
     */
    public String getDescription() {
        return description;
    }

    /**
     * method for changing the description of this track.
     *
     * @param description for this track
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * method for getting the paper type of this track.
     *
     * @return the allowed paper type for submission for this track
     */
    public PaperType getPaperType() {
        return paperType;
    }

    /**
     * method for changing the paper type of this track.
     *
     * @param paperType that is allowed for this track
     */
    public void setPaperType(PaperType paperType) {
        this.paperType = paperType;
    }

    /**
     * method for getting the deadline for submission of this track.
     *
     * @return the deadline for submission for this track
     */
    public Date getSubmitDeadline() {
        return submitDeadline;
    }

    /**
     * method for changing the deadline for submission of this track.
     *
     * @param submitDeadline for this track
     */
    public void setSubmitDeadline(Date submitDeadline) {
        this.submitDeadline = submitDeadline;
    }

    /**
     * method for getting the deadline for review of this track.
     *
     * @return the deadline for review for this track
     */
    public Date getReviewDeadline() {
        return reviewDeadline;
    }

    /**
     * method for getting the deadline for reviewing of this track.
     *
     * @param reviewDeadline for this track
     */
    public void setReviewDeadline(Date reviewDeadline) {
        this.reviewDeadline = reviewDeadline;
    }

    /**
     * method for getting the event of this track.
     *
     * @return the event this track belongs to
     */
    public Event getEvent() {
        return event;
    }

    /**
     * method for change the event of this track.
     *
     * @param event which this track belongs to
     */
    public void setEvent(Event event) {
        this.event = event;
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