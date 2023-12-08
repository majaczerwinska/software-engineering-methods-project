package nl.tudelft.sem.template.domain;

import org.openapitools.jackson.nullable.JsonNullable;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Event {

    @Id
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private JsonNullable<String> description = JsonNullable.undefined();

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isCancelled;
}

