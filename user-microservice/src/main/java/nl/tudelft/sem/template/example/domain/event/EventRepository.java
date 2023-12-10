package nl.tudelft.sem.template.example.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//change event to correct event class when it gets created
@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Override
    List<Event> findAll();

    List<Event> findAllByUserId(Integer id);

    @Override
    Optional<Event> findById(Integer id);

    boolean existsById(Integer id);
//return type?
    @Override
    Event save(Event event);




}
