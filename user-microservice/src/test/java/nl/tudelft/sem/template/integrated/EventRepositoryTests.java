package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventDescription;
import nl.tudelft.sem.template.domain.event.EventName;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.event.IsCancelled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain
// beans.
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventRepositoryTests {

    @Autowired
    private transient EventRepository eventRepository;

    @Test
    @Transactional
    public void findByOptionalParamsTest() {
        LocalDate startDate0 = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate0 = LocalDate.parse("2024-01-10T19:36:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate startDate1 = LocalDate.parse("2024-02-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate endDate1 = LocalDate.parse("2024-02-10T19:36:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate startDate2 = LocalDate.parse("2024-03-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        Event event0 = eventRepository.save(new Event(
                startDate0, endDate0, new IsCancelled(false), new EventName("name"), new EventDescription("desc")));
        Event event1 = eventRepository.save(new Event(
                startDate0, endDate0, new IsCancelled(true), new EventName("NAME"), new EventDescription("desc")));
        Event event2 = eventRepository.save(new Event(
                startDate1, endDate1, new IsCancelled(false), new EventName("name"), new EventDescription("desc")));
        Event event3 = eventRepository.save(new Event(
                startDate2, endDate1, new IsCancelled(false), new EventName("NAME"), new EventDescription("desc")));

        List<Event> all = eventRepository.findByOptionalParams(null, null, null, null, null, null);
        assertTrue(all.contains(event0));
        assertTrue(all.contains(event1));
        assertTrue(all.contains(event2));
        assertTrue(all.contains(event3));

        List<Event> cancelled = eventRepository.findByOptionalParams(null, null, null, null, new IsCancelled(true), null);
        assertFalse(cancelled.contains(event0));
        assertTrue(cancelled.contains(event1));
        assertFalse(cancelled.contains(event2));
        assertFalse(cancelled.contains(event3));

        List<Event> withNameCancelled = eventRepository.findByOptionalParams(null, null, null, null, new IsCancelled(true), new EventName("NAME"));
        assertFalse(withNameCancelled.contains(event0));
        assertTrue(withNameCancelled.contains(event1));
        assertFalse(withNameCancelled.contains(event2));
        assertFalse(withNameCancelled.contains(event3));

        List<Event> beforeAndAfter = eventRepository.findByOptionalParams(LocalDate.parse("2024-03-09T19:25:47Z", DateTimeFormatter.ISO_DATE_TIME), LocalDate.parse("2024-01-09T19:27:47Z", DateTimeFormatter.ISO_DATE_TIME), null, null, null, null);
        assertFalse(beforeAndAfter.contains(event0));
        assertFalse(beforeAndAfter.contains(event1));
        assertTrue(beforeAndAfter.contains(event2));
        assertFalse(beforeAndAfter.contains(event3));
    }
}
