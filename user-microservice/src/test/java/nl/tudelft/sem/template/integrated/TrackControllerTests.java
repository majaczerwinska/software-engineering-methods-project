package nl.tudelft.sem.template.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import nl.tudelft.sem.template.Application;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.controllers.TrackController;
import nl.tudelft.sem.template.domain.event.Event;
import nl.tudelft.sem.template.domain.track.Description;
import nl.tudelft.sem.template.domain.track.PaperRequirement;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.model.Track;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.RoleService;
import nl.tudelft.sem.template.services.TrackService;
import nl.tudelft.sem.template.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles("test")
public class TrackControllerTests {

    private static Email userEmail;
    private Track modelTrack;
    private nl.tudelft.sem.template.domain.track.Track domainTrack;
    @Mock
    private UserService userService;

    @Mock
    private AuthManager authManager;

    @Mock
    private AttendeeService attendeeService;

    @Mock
    private RoleService roleService;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private transient TrackController trackController;

    /**
     * create the testing domainTrack and modelTrack.
     */
    @BeforeEach
    public void setup() {
        userEmail = new Email("test@test.net");
        Title title = new Title("Track Title");
        Description description = new Description("Track Description");
        PaperRequirement paperRequirement = new PaperRequirement(PaperType.FULL_PAPER);
        LocalDate subDeadline = LocalDate.now().plusDays(7);
        LocalDate reviewDeadline = LocalDate.now().plusDays(14);
        domainTrack = new nl.tudelft.sem.template.domain.track.Track(title, description,
                paperRequirement, subDeadline, reviewDeadline, new Event(52L));
        modelTrack = domainTrack.toModelTrack();
    }

    @Test
    public void addTrackWithoutPermission() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(0)
        )).thenReturn(false);

        // Run
        ResponseEntity<Track> response = trackController.addTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(0));
    }

    @Test
    public void addTrackInvalidTrack() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(0)
        )).thenReturn(true);

        when(trackService.createTrack(modelTrack.getTitle(), modelTrack.getDescription(),
                modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId()))
                .thenThrow(new IllegalArgumentException());

        // Run
        ResponseEntity<Track> response = trackController.addTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(0));
        verify(trackService, times(1)).createTrack(modelTrack.getTitle(),
                modelTrack.getDescription(), modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId());
    }

    @Test
    public void addTrackSuccess() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(0)
        )).thenReturn(true);

        when(trackService.createTrack(modelTrack.getTitle(), modelTrack.getDescription(),
                modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId())).thenReturn(domainTrack);

        // Run
        ResponseEntity<Track> response = trackController.addTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(modelTrack, response.getBody());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(0));
        verify(trackService, times(1)).createTrack(modelTrack.getTitle(),
                modelTrack.getDescription(), modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId());
    }

    @Test
    public void updateTrackWithoutPermission() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1)
        )).thenReturn(false);

        // Run
        ResponseEntity<Void> response = trackController.updateTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1));
    }

    @Test
    public void updateTrackInvalidTrack() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1)
        )).thenReturn(true);

        when(trackService.updateTrack(modelTrack.getId(), modelTrack.getTitle(), modelTrack.getDescription(),
                modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId())).thenThrow(new IllegalArgumentException());

        // Run
        ResponseEntity<Void> response = trackController.updateTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1));
        verify(trackService, times(1)).updateTrack(modelTrack.getId(),
                modelTrack.getTitle(), modelTrack.getDescription(), modelTrack.getSubmitDeadline(),
                modelTrack.getReviewDeadline(), modelTrack.getPaperType(), modelTrack.getEventId());
    }

    @Test
    public void updateTrackNonExistTrack() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1)
        )).thenReturn(true);

        when(trackService.updateTrack(modelTrack.getId(), modelTrack.getTitle(), modelTrack.getDescription(),
                modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId())).thenThrow(new NoSuchElementException());

        // Run
        ResponseEntity<Void> response = trackController.updateTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1));
        verify(trackService, times(1)).updateTrack(modelTrack.getId(),
                modelTrack.getTitle(), modelTrack.getDescription(), modelTrack.getSubmitDeadline(),
                modelTrack.getReviewDeadline(), modelTrack.getPaperType(), modelTrack.getEventId());
    }

    @Test
    public void updateTrackSuccess() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1)
        )).thenReturn(true);

        when(trackService.updateTrack(modelTrack.getId(), modelTrack.getTitle(), modelTrack.getDescription(),
                modelTrack.getSubmitDeadline(), modelTrack.getReviewDeadline(),
                modelTrack.getPaperType(), modelTrack.getEventId())).thenReturn(domainTrack);

        // Run
        ResponseEntity<Void> response = trackController.updateTrack(modelTrack);

        // Check
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(modelTrack.getEventId()),
                eq(modelTrack.getId()),
                eq(1));
        verify(trackService, times(1)).updateTrack(modelTrack.getId(),
                modelTrack.getTitle(), modelTrack.getDescription(), modelTrack.getSubmitDeadline(),
                modelTrack.getReviewDeadline(), modelTrack.getPaperType(), modelTrack.getEventId());
    }

    @Test
    public void deleteTrackWithoutPermission() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0)
        )).thenReturn(false);

        when(trackService.getTrackById(33L)).thenReturn(domainTrack);

        // Run
        ResponseEntity<Void> response = trackController.deleteTrack(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0));
    }

    @Test
    public void deleteTrackNullId() {
        // Run
        ResponseEntity<Void> response = trackController.deleteTrack(null);
        // Check
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Verify
        verify(roleService, times(0)).hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0));
    }

    @Test
    public void deleteTrackInvalidTrackId() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0)
        )).thenReturn(true);
        when(trackService.getTrackById(33L)).thenReturn(domainTrack);
        when(trackService.deleteTrackById(eq(33L))).thenThrow(new IllegalArgumentException());

        // Run
        ResponseEntity<Void> response = trackController.deleteTrack(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0));
        verify(trackService, times(1)).getTrackById(eq(33L));
        verify(trackService, times(1)).deleteTrackById(eq(33L));
    }

    @Test
    public void deleteTrackNonExistTrack() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0)
        )).thenReturn(true);
        when(trackService.getTrackById(33L)).thenReturn(domainTrack);
        when(trackService.deleteTrackById(eq(33L))).thenThrow(new NoSuchElementException());

        // Run
        ResponseEntity<Void> response = trackController.deleteTrack(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0));
        verify(trackService, times(1)).getTrackById(eq(33L));
        verify(trackService, times(1)).deleteTrackById(eq(33L));
    }

    @Test
    public void deleteTrackSuccess() {
        // Mock
        when(roleService.hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0)
        )).thenReturn(true);
        when(trackService.getTrackById(eq(33L))).thenReturn(domainTrack);
        when(trackService.deleteTrackById(eq(33L))).thenReturn(domainTrack);

        // Run
        ResponseEntity<Void> response = trackController.deleteTrack(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify
        verify(roleService, times(1)).hasPermission(
                any(AuthManager.class),
                eq(domainTrack.getEvent().getId()),
                eq(33L),
                eq(0));
        verify(trackService, times(1)).getTrackById(eq(33L));
        verify(trackService, times(1)).deleteTrackById(eq(33L));
    }

    @Test
    public void getTrackByIdWithoutPermission() {
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(false);

        // Run
        ResponseEntity<Track> response = trackController.getTrackByID(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
    }

    @Test
    public void getTrackByIdInvalidTrack() {
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);
        when(trackService.getTrackById(33L)).thenThrow(new IllegalArgumentException());

        // Run
        ResponseEntity<Track> response = trackController.getTrackByID(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
        verify(trackService, times(1)).getTrackById(eq(33L));
    }

    @Test
    public void getTrackByIdNonExistTrack() {
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);
        when(trackService.getTrackById(33L)).thenThrow(new NoSuchElementException());

        // Run
        ResponseEntity<Track> response = trackController.getTrackByID(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
        verify(trackService, times(1)).getTrackById(eq(33L));
    }

    @Test
    public void getTrackByIdSuccess() {
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);
        when(trackService.getTrackById(33L)).thenReturn(domainTrack);

        // Run
        ResponseEntity<Track> response = trackController.getTrackByID(Math.toIntExact(33L));

        // Check
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(modelTrack, response.getBody());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
        verify(trackService, times(1)).getTrackById(eq(33L));
    }

    @Test
    public void getTrackWithoutPermission() {
        Long eventId = 52L;
        Title title = new Title("Test title");
        PaperType p = PaperType.FULL_PAPER;
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(false);

        // Run
        ResponseEntity<List<Track>> response = trackController.getTrack(title.toString(), Math.toIntExact(eventId), p);

        // Check
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
    }

    @Test
    public void getTrackByTitleInEvent() {
        Long eventId = 52L;
        Title title = new Title("Test title");
        PaperType p = PaperType.FULL_PAPER;
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);
        when(trackService.getTrackByTitleInEvent(title, eventId)).thenReturn(domainTrack);
        // Run
        ResponseEntity<List<Track>> response = trackController.getTrack(title.toString(), Math.toIntExact(eventId), p);

        // Check
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(modelTrack, Objects.requireNonNull(response.getBody()).get(0));
        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
        verify(trackService, times(1)).getTrackByTitleInEvent(title, eventId);
    }

    @Test
    public void getTrackByTitle() {
        Title title = new Title("Test title");
        PaperType p = PaperType.FULL_PAPER;
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);
        when(trackService.getTrackByTitle(title)).thenReturn(Collections.singletonList(domainTrack));
        // Run
        ResponseEntity<List<Track>> response = trackController.getTrack(title.toString(), null, p);

        // Check
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(modelTrack, Objects.requireNonNull(response.getBody()).get(0));
        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
        verify(trackService, times(1)).getTrackByTitle(title);

    }

    @Test
    public void getTrackByEvent() {
        Long eventId = 52L;
        PaperType p = PaperType.FULL_PAPER;
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);
        when(trackService.getTrackByParentEvent(eventId)).thenThrow(new NoSuchElementException());

        // Run
        ResponseEntity<List<Track>> response = trackController.getTrack(null, Math.toIntExact(eventId), p);

        // Check
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
        verify(trackService, times(1)).getTrackByParentEvent(eventId);

    }

    @Test
    public void getTrackNullInput() {
        PaperType p = PaperType.FULL_PAPER;
        // Mock
        when(authManager.getEmail()).thenReturn(userEmail.toString());
        when(userService.userExistsByEmail(userEmail)).thenReturn(true);

        // Run
        ResponseEntity<List<Track>> response = trackController.getTrack(null, null, p);

        // Check
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify
        verify(authManager, times(1)).getEmail();
        verify(userService, times(1)).userExistsByEmail(userEmail);
    }
}
