package nl.tudelft.sem.template.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

//    @Column(nullable = false)
//    private Long userId;
//
//    @Column(nullable = false)
//    private Long eventId;
//
//    @Column
//    private JsonNullable<Long> trackId = JsonNullable.undefined();
//
//    @Enumerated(EnumType.ORDINAL)
//    @Column(nullable = false)
//    private RoleTitle roleTitle;
//
//    @Column(nullable = false)
//    private Boolean confirmed;
}

