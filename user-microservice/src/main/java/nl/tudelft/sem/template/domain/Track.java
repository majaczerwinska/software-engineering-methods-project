package nl.tudelft.sem.template.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Track {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false)
//    private String description;
//
//    @Enumerated(EnumType.ORDINAL)
//    @Column(nullable = false)
//    private PaperType paperType;
//
//    @Column(nullable = false)
//    private String submitDeadline;
//
//    @Column(nullable = false)
//    private String reviewDeadline;
//
//    @Column(nullable = false)
//    private Long eventId;

}

