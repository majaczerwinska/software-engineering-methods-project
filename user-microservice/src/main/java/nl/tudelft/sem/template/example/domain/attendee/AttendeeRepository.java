package nl.tudelft.sem.template.example.domain.attendee;

import nl.tudelft.sem.template.example.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttendeeRepository extends JpaRepository<Event, Long> {



}
