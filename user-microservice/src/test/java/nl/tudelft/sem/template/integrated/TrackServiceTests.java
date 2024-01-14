package nl.tudelft.sem.template.integrated;

import nl.tudelft.sem.template.Application;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private static Track fullTrack;
    private static Title title;
    @Mock
    private transient TrackRepository trackRepository;
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
        fullTrack = new Track(title, description, requirement, subDeadline, reviewDeadline, 2023L);
    }

    @Test
    public void createTrackNullTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(null));
        assertEquals("Null reference for track", exception.getMessage());
    }

    @Test
    public void createTrackInvalidIdTest() {
        fullTrack.setId(-1L);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(fullTrack));
        assertEquals(invalidId, exception.getMessage());

        fullTrack.setTitle(null);
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(fullTrack));
        assertEquals(invalidId, exception.getMessage());
    }

    @Test
    public void createTrackTitleInvalidTest() {
        fullTrack.setTitle(null);
        fullTrack.setParentEventId(-1);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(fullTrack));
        assertEquals(nullTitle, exception.getMessage());

        fullTrack.setTitle(new Title(null));
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(fullTrack));
        assertEquals(nullTitle, exception.getMessage());
    }

    @Test
    public void createTrackParentIdInvalidTest() {
        fullTrack.setParentEventId(-1);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(fullTrack));
        assertEquals("Invalid parent event id", exception.getMessage());
    }

    @Test
    public void createTrackIdExistTest() {
        when(trackRepository.existsById(fullTrack.getId())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.createTrack(fullTrack));
        assertEquals("Track with id:" + fullTrack.getId() + " already exist.", exception.getMessage());
        verify(trackRepository, times(1)).existsById(fullTrack.getId());
    }

    @Test
    public void createTrackSuccessTest() {
        when(trackRepository.save(fullTrack)).thenReturn(fullTrack);
        assertEquals(trackService.createTrack(fullTrack), fullTrack);
        verify(trackRepository, times(1)).save(fullTrack);
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
        fullTrack.setTitle(null);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.updateTrack(fullTrack));
        assertEquals(nullTitle, exception.getMessage());

        fullTrack.setTitle(new Title(null));
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.updateTrack(fullTrack));
        assertEquals(nullTitle, exception.getMessage());
        verify(trackRepository, times(0))
                .existsByTitleAndParentEventId(title, 5233L);
    }

    @Test
    public void updateTrackAlreadyExistTest() {
        when(trackRepository.existsByTitleAndParentEventId(fullTrack.getTitle(), fullTrack.getParentEventId()))
                .thenReturn(true);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trackService.updateTrack(fullTrack));
        assertEquals("Track with this title already exist in the event.", exception.getMessage());
        verify(trackRepository, times(1))
                .existsByTitleAndParentEventId(fullTrack.getTitle(), fullTrack.getParentEventId());
        verify(trackRepository, times(0)).existsById(5233L);
    }

    @Test
    public void updateTrackNoIdExistTest() {
        when(trackRepository.existsByTitleAndParentEventId(fullTrack.getTitle(),
                fullTrack.getParentEventId())).thenReturn(false);
        when(trackRepository.existsById(fullTrack.getId())).thenReturn(false);
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.updateTrack(fullTrack));
        assertEquals("Track with id:" + fullTrack.getId() + " does not exist.", exception.getMessage());
        verify(trackRepository, times(1))
                .existsByTitleAndParentEventId(fullTrack.getTitle(), fullTrack.getParentEventId());
        verify(trackRepository, times(1)).existsById(fullTrack.getId());
        verify(trackRepository, times(0)).save(fullTrack);
    }

    @Test
    public void updateTrackSuccessTest() {
        when(trackRepository.existsByTitleAndParentEventId(fullTrack.getTitle(), fullTrack.getParentEventId()))
                .thenReturn(false);
        when(trackRepository.existsById(fullTrack.getId())).thenReturn(true);
        when(trackRepository.save(fullTrack)).thenReturn(fullTrack);
        assertEquals(trackService.updateTrack(fullTrack), fullTrack);
        verify(trackRepository, times(1))
                .existsByTitleAndParentEventId(fullTrack.getTitle(), fullTrack.getParentEventId());
        verify(trackRepository, times(1)).existsById(fullTrack.getId());
        verify(trackRepository, times(1)).save(fullTrack);
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
        long trackId = 5233L;
        when(trackRepository.findById(trackId)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackById(trackId));
        assertEquals("Track with ID: " + trackId + " does not exist.", exception.getMessage());
        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    public void getTrackByIdSuccessTest() {
        long trackId = 5233L;
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
        assertEquals("Invalid parent id", exception.getMessage());
    }

    @Test
    public void getTrackByParentEventNoTrackTest() {
        when(trackRepository.findByParentEventId(5233L)).thenReturn(new ArrayList<>());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackByParentEvent(5233L));

        assertEquals("No Track exist in event:" + 5233L, exception.getMessage());
        verify(trackRepository, times(1)).findByParentEventId(5233L);
    }

    @Test
    public void getTrackByParentEventSuccessTest() {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(fullTrack);
        when(trackRepository.findByParentEventId(5233L)).thenReturn(tracks);
        assertEquals(trackService.getTrackByParentEvent(5233L), tracks);
        verify(trackRepository, times(1)).findByParentEventId(5233L);
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
        when(trackRepository.findByTitleAndParentEventId(title, 5233L)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> trackService.getTrackByTitleInEvent(title, 5233L));

        assertEquals("Track with title:" + title.toString()
                + " does not exist  in event: " + 5233L, exception.getMessage());
        verify(trackRepository, times(1)).findByTitleAndParentEventId(title, 5233L);
    }

    @Test
    public void getTrackByTitleInEventSuccessTest() {
        when(trackRepository.findByTitleAndParentEventId(title, 5233L)).thenReturn(Optional.of(fullTrack));
        assertEquals(trackService.getTrackByTitleInEvent(title, 5233L), fullTrack);
        verify(trackRepository, times(1)).findByTitleAndParentEventId(title, 5233L);
    }
}
