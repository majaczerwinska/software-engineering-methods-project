package nl.tudelft.sem.template.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import nl.tudelft.sem.template.domain.track.Track;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * the class for an event which can be created by users.
 */
@Entity
public class Event {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description")
    private JsonNullable<String> description = JsonNullable.undefined();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Boolean isCancelled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Track> trackList;

}
