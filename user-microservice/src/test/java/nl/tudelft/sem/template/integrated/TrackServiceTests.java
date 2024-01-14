package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.event.EventRepository;
import nl.tudelft.sem.template.domain.track.Description;
import nl.tudelft.sem.template.domain.track.PaperRequirement;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.services.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * integrated tests for track service.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles("test")
public class TrackServiceTests {

    private static final String nullTitle = "Null reference for track title";
    private static final String invalidId = "Invalid track id";
    private static final String invalidEventId = "Invalid event id";
    private static Track fullTrack;
    private static Title title;
    @Mock
    private transient TrackRepository trackRepository;
    @Mock
    private transient EventRepository eventRepository;
    @InjectMocks
    private transient TrackService trackService;

    /**
     * set up a track using all arg constructor.
     */
    @BeforeEach
    public void setup() {
        title = new Title("test title");
        Description description = new Description("Description for full track");
        PaperRequirement requirement = new PaperRequirement(PaperType.FULL_PAPER);
        LocalDate subDeadline = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate reviewDeadline = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        fullTrack = new Track(title, description, requirement, subDeadline, reviewDeadline, new Event(2023L));
    }

    @Test
    public void createTrackInvalidEventIdTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(null, null,null,null,
                        PaperType.FULL_PAPER, -1L));
        assertEquals(invalidEventId, exception.getMessage());
    }

    @Test
    public void createTrackInvalidEventTest() {
        when(eventRepository.findById(22L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(null, null,null,null,
                        PaperType.FULL_PAPER, 22L));
        assertEquals(nullTitle, exception.getMessage());
    }

    @Test
    public void createTrackTitleInvalidTest() {
        when(eventRepository.findById(22L)).thenReturn(Optional.of(new Event(2L)));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(null, null,null,null,
                        PaperType.FULL_PAPER, 2L));
        assertEquals(nullTitle, exception.getMessage());
    }

    @Test
    public void createTrackSuccessTest() {
        when(eventRepository.findById(22L)).thenReturn(Optional.of(new Event(2023L)));

        Track track = trackService.createTrack(title.toString(), null,null,null,
                PaperType.FULL_PAPER, 22L);
        assertEquals(fullTrack, track);
    }

    @Test
    public void deleteTrackByIdInvalidIdTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.deleteTrackById(-1L));
        assertEquals(invalidId, exception.getMessage());
        verify(trackRepository, times(0)).existsById(5233L);
    }

    @Test
    public void deleteTrackByIdNoTrackTest() {
        when(trackRepository.findById(5233L)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.deleteTrackById(5233L));
        assertEquals("Track with id:" + 5233L + " does not exist.", exception.getMessage());
        verify(trackRepository, times(1)).findById(5233L);
        verify(trackRepository, times(0)).delete(fullTrack);
    }

    @Test
    public void deleteTrackByIdSuccessTest() {
        when(trackRepository.findById(5233L)).thenReturn(Optional.of(fullTrack));
        trackService.deleteTrackById(5233L);
        verify(trackRepository, times(1)).findById(5233L);
        verify(trackRepository, times(1)).delete(fullTrack);
    }

    @Test
    public void updateTrackInvalidTitleTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.updateTrack(0L,null, null,null,null,
                        PaperType.FULL_PAPER, 2L));
        assertEquals(nullTitle, exception.getMessage());
    }

    @Test
    public void updateTrackAlreadyExistTest() {
        fullTrack.setId(1L);
        when(trackRepository.findByTitleAndEventId(eq(title), eq(2L)))
                .thenReturn(Optional.ofNullable(fullTrack));
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.updateTrack(0L,title.toString(), null,null,null,
                        PaperType.FULL_PAPER, 2L));
        assertEquals("Track with this title already exist in the event.", exception.getMessage());
        verify(trackRepository, times(1)).findByTitleAndEventId(eq(title), eq(2L));
        verify(trackRepository, times(0)).existsById(eq(0L));
    }

    @Test
    public void updateTrackNoIdExistTest() {
        when(trackRepository.findByTitleAndEventId(eq(title), eq(2L)))
                .thenReturn(Optional.empty());
        when(trackRepository.existsById(0L)).thenReturn(false);
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.updateTrack(0L,title.toString(), null,null,null,
                        PaperType.FULL_PAPER, 2L));
        assertEquals("Track with id:" + 0L + " does not exist.", exception.getMessage());
        verify(trackRepository, times(1)).findByTitleAndEventId(eq(title), eq(2L));
        verify(trackRepository, times(1)).existsById(eq(0L));
        verify(trackRepository, times(0)).save(eq(fullTrack));
    }

    @Test
    public void updateTrackEventNotExistTest() {
        when(trackRepository.findByTitleAndEventId(eq(title), eq(2L)))
                .thenReturn(Optional.empty());
        when(trackRepository.existsById(0L)).thenReturn(true);
        when(eventRepository.findById(eq(2L))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.updateTrack(0L, title.toString(), null, null, null,
                        PaperType.FULL_PAPER, 2L));
        assertEquals(invalidEventId, exception.getMessage());

        // Verify the correct method is invoked
        verify(trackRepository, times(1)).findByTitleAndEventId(eq(title), eq(2L));
        verify(trackRepository, times(1)).existsById(eq(0L));
        verify(eventRepository, times(1)).findById(eq(2L));
    }

    @Test
    public void updateTrackSuccessTest() {
        when(trackRepository.findByTitleAndEventId(eq(title), eq(2L)))
                .thenReturn(Optional.empty());
        when(trackRepository.existsById(0L)).thenReturn(true);
        when(eventRepository.findById(fullTrack.getEvent().getId())).thenReturn(Optional.of(new Event(22L)));
        when(trackRepository.save(fullTrack)).thenReturn(fullTrack);
        Track track = new Track(0L, title, null, new PaperRequirement(PaperType.FULL_PAPER), null,
                null, fullTrack.getEvent());
        assertEquals(trackService.updateTrack(0L,title.toString(), null,null,
                null, PaperType.FULL_PAPER, fullTrack.getEvent().getId()), track);
        verify(trackRepository, times(1)).findByTitleAndEventId(eq(title), eq(2L));
        verify(trackRepository, times(1)).existsById(eq(0L));
        verify(trackRepository, times(1)).save(eq(fullTrack));
    }

    @Test
    public void getTrackByIdInvalidIdTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackById(-1L));
        assertEquals(invalidId, exception.getMessage());
        verify(trackRepository, times(0)).existsById(5233L);
    }

    @Test
    public void getTrackByIdNoTrackTest() {
        Long trackId = 5233L;
        when(trackRepository.findById(trackId)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackById(trackId));
        assertEquals("Track with ID: " + trackId + " does not exist.", exception.getMessage());
        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    public void getTrackByIdSuccessTest() {
        Long trackId = 5233L;
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(fullTrack));
        assertEquals(trackService.getTrackById(trackId), fullTrack);
        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    public void getTrackByTitleInvalidTitleTest() {
        Title testTitle = new Title(null);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByTitle(null));
        assertEquals(nullTitle, exception.getMessage());

        exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByTitle(testTitle));
        assertEquals(nullTitle, exception.getMessage());
    }

    @Test
    public void getTrackByTitleNoTrackTest() {
        when(trackRepository.findByTitle(title)).thenReturn(new ArrayList<>());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackByTitle(title));
        assertEquals("Track with title:" + title + " can not be found.", exception.getMessage());
        verify(trackRepository, times(1)).findByTitle(title);
    }

    @Test
    public void getTrackByTitleSuccessTest() {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(fullTrack);
        when(trackRepository.findByTitle(title)).thenReturn(tracks);
        assertEquals(trackService.getTrackByTitle(title), tracks);
        verify(trackRepository, times(1)).findByTitle(title);
    }

    @Test
    public void getTrackByParentEventInvalidIdTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByParentEvent(-1L));
        assertEquals(invalidEventId, exception.getMessage());
    }

    @Test
    public void getTrackByParentEventNoTrackTest() {
        when(trackRepository.findByEventId(5233L)).thenReturn(new ArrayList<>());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackByParentEvent(5233L));

        assertEquals("No Track exist in event: " + 5233L, exception.getMessage());
        verify(trackRepository, times(1)).findByEventId(5233L);
    }

    @Test
    public void getTrackByParentEventSuccessTest() {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(fullTrack);
        when(trackRepository.findByEventId(5233L)).thenReturn(tracks);
        assertEquals(trackService.getTrackByParentEvent(5233L), tracks);
        verify(trackRepository, times(1)).findByEventId(5233L);
    }

    @Test
    public void getTrackByTitleInEventInvalidIdTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByTitleInEvent(title, -1L));
        assertEquals("Invalid parent id", exception.getMessage());

        exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByTitleInEvent(null, -1L));
        assertEquals("Invalid parent id", exception.getMessage());
    }

    @Test
    public void getTrackByTitleInEventInvalidTitleTest() {
        Title testTitle = new Title(null);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByTitleInEvent(null, 5233L));
        assertEquals(nullTitle, exception.getMessage());

        exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.getTrackByTitleInEvent(testTitle, 5233L));
        assertEquals(nullTitle, exception.getMessage());
    }

    @Test
    public void getTrackByTitleInEventNoTrackTest() {
        when(trackRepository.findByTitleAndEventId(title, 5233L)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackByTitleInEvent(title, 5233L));

        assertEquals("Track with title:" + title.toString()
                + " does not exist  in event: " + 5233L, exception.getMessage());
        verify(trackRepository, times(1)).findByTitleAndEventId(title, 5233L);
    }

    @Test
    public void getTrackByTitleInEventSuccessTest() {
        when(trackRepository.findByTitleAndEventId(title, 5233L)).thenReturn(Optional.of(fullTrack));
        assertEquals(trackService.getTrackByTitleInEvent(title, 5233L), fullTrack);
        verify(trackRepository, times(1)).findByTitleAndEventId(title, 5233L);
    }
}
