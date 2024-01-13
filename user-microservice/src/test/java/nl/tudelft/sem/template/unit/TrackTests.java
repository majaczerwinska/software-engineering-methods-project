package nl.tudelft.sem.template.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.transaction.Transactional;
import nl.tudelft.sem.template.domain.track.Description;
import nl.tudelft.sem.template.domain.track.PaperRequirement;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.domain.track.TrackRepository;
import nl.tudelft.sem.template.model.PaperType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@Transactional
@Commit
@SpringBootTest
public class TrackTests {

    private static Track nullTrack;
    private static Track fullTrack1;
    private static Track fullTrack2;
    private static Track emptyTrack;
    private static Title title;

    @Autowired
    private TrackRepository trackRepository;

    /**
     * Setups the variables for the tests.
     */
    @BeforeEach
    public void setup() {
        title = new Title("Title for full track");
        Description description = new Description("Description for full track");
        PaperRequirement requirement = new PaperRequirement(PaperType.FULL_PAPER);
        LocalDate subDeadline = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate reviewDeadline = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);

        nullTrack = null;
        fullTrack1 = new Track(title, description, requirement, subDeadline, reviewDeadline, 2023L);
        fullTrack2 = new Track(title, description, requirement, subDeadline, reviewDeadline, 2023L);
        emptyTrack = new Track();
    }

    @Test
    void allConstructorTest() {
        assertNotNull(fullTrack1);
        assertNotNull(fullTrack2);
        assertNotNull(emptyTrack);
        assertNull(nullTrack);
        Track savedTrack = trackRepository.save(fullTrack2);
        assertNotNull(savedTrack.getId());
    }

    @Test
    void allArgConstructorTest() {
        Description descriptionForTest = new Description("Description for test");
        PaperRequirement paperRequirement = new PaperRequirement(PaperType.POSITION_PAPER);
        LocalDate subDeadlineForTest = LocalDate.parse("2029-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate reviewDeadlineForTest = LocalDate.parse("2024-07-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        Track track = new Track(title, descriptionForTest, paperRequirement,
                subDeadlineForTest, reviewDeadlineForTest, 999L);

        assertNotEquals(fullTrack1, track);

        assertEquals(track.getTitle().toString(), title.toString());
        assertEquals(fullTrack1.getTitle().toString(), title.toString());

        assertEquals(track.getDescription().toString(), descriptionForTest.toString());
        assertNotEquals(fullTrack1.getDescription().toString(), descriptionForTest.toString());

        assertEquals(track.getPaperType().toPaperType(), paperRequirement.toPaperType());
        assertNotEquals(fullTrack1.getPaperType().toPaperType(), paperRequirement.toPaperType());

        assertEquals(track.getReviewDeadline(), reviewDeadlineForTest);
        assertNotEquals(fullTrack1.getReviewDeadline(), reviewDeadlineForTest);

        assertEquals(track.getSubmitDeadline(), subDeadlineForTest);
        assertNotEquals(fullTrack1.getSubmitDeadline(), subDeadlineForTest);

        assertEquals(track.getParentEventId(), 999L);
        assertNotEquals(fullTrack1.getParentEventId(), 999L);
    }

    @Test
    void equalsSelfTest() {
        assertEquals(fullTrack1, fullTrack1);
    }

    @Test
    void equalsSameTest() {
        assertEquals(fullTrack1, fullTrack2);
        assertEquals(fullTrack2, fullTrack1);
    }

    @Test
    void equalsEmptyTest() {
        assertNotEquals(fullTrack1, emptyTrack);
        emptyTrack.setTitle(null);
        assertNotEquals(emptyTrack, fullTrack2);
    }

    @Test
    void equalsDiffInstantTest() {
        assertNotEquals(fullTrack1, "ABD");
        assertNotEquals(emptyTrack, title);
    }

    @Test
    void hashCodeTest() {
        assertEquals(fullTrack1.hashCode(), fullTrack2.hashCode());
    }

    @Test
    void allSettersTest() {
        fullTrack1.setId(1351L);
        assertEquals(fullTrack1.getId(), 1351L);

        Title titleAfterSetter = new Title("Title after setter");
        fullTrack1.setTitle(titleAfterSetter);
        assertEquals(fullTrack1.getTitle().toString(), titleAfterSetter.toString());

        Description description = new Description("Description after setter");
        fullTrack1.setDescription(description);
        assertEquals(fullTrack1.getDescription().toString(), description.toString());

        PaperRequirement requirement = new PaperRequirement(PaperType.SHORT_PAPER);
        fullTrack1.setPaperType(requirement);
        assertEquals(fullTrack1.getPaperType().toPaperType(), requirement.toPaperType());

        LocalDate date = LocalDate.parse("2020-09-23T18:56:47Z", DateTimeFormatter.ISO_DATE_TIME);
        fullTrack1.setSubmitDeadline(date);
        assertEquals(fullTrack1.getSubmitDeadline(), date);
        assertNotEquals(fullTrack1.getReviewDeadline(), date);
        fullTrack1.setReviewDeadline(date);
        assertEquals(fullTrack1.getReviewDeadline(), date);

        fullTrack1.setParentEventId(123L);
        assertEquals(fullTrack1.getParentEventId(), 123L);
    }

    @Test
    void modelConverterTest() {
        nl.tudelft.sem.template.model.Track modelTrack = new nl.tudelft.sem.template.model.Track();
        modelTrack.setEventId(2023L);
        modelTrack.setTitle("Title for full track");
        modelTrack.setDescription("Description for full track");
        modelTrack.setPaperType(PaperType.FULL_PAPER);
        LocalDate subDeadline = LocalDate.parse("2024-01-09T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        LocalDate reviewDeadline = LocalDate.parse("2024-01-10T19:26:47Z", DateTimeFormatter.ISO_DATE_TIME);
        modelTrack.setSubmitDeadline(subDeadline.toString());
        modelTrack.setReviewDeadline(reviewDeadline.toString());
        assertEquals(fullTrack1.toModelTrack(), modelTrack);
        assertEquals(fullTrack1, new Track(modelTrack));
    }

}
