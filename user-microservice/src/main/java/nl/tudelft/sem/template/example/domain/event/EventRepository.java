package nl.tudelft.sem.template.example.domain.event;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Override
    List<Event> findAll();

    /**
     * idk I thought we may want this type of methods for filtering the events, but that would mean
     * that events are associated with a user. This would imply adding a userId field in the Event class or
     * some relationship mapping in the Event class.
     */
    //List<Event> findAllByUserId(Long id);

    //TODO: add more custom queries??

    @Override
    Optional<Event> findById(Long id);

    boolean existsById(Long id);

    @Override
    Event save(Event event);


}
