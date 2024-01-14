package nl.tudelft.sem.template.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import nl.tudelft.sem.template.api.TrackApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.model.Track;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.RoleService;
import nl.tudelft.sem.template.services.TrackService;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for methods related to the Track entity.
 */
// The redundancy of literals here is justified for the queries and the
// parameter alias.
@RestController
public class TrackController implements TrackApi {
    private final transient AuthManager authManager;
    private final transient AttendeeService attendeeService;
    private final transient TrackService trackService;
    private final transient UserService userService;
    private final transient RoleService roleService;

    /**
     * Constructs a new instance of Track Controller.
     *
     * @param authManager     Used for authentication-related checks.
     * @param attendeeService The service for managing attendee-related operations.
     * @param trackService    The service for managing track-related operations.
     * @param userService     The service for managing user-related operations.
     * @param roleService     The service for managing role-related operations.
     */
    @Autowired
    public TrackController(AuthManager authManager, AttendeeService attendeeService, RoleService roleService,
                           TrackService trackService, UserService userService) {
        this.authManager = authManager;
        this.attendeeService = attendeeService;
        this.trackService = trackService;
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Create a new Track.
     *
     * @param track the RequestBody to create a new Track
     * @return ResponseEntity of new Track or an error response
     */
    @Override
    @Transactional
    public ResponseEntity<Track> addTrack(Track track) {
        try {
            if (!roleService.hasPermission(userService, authManager, attendeeService,
                    track.getEventId(), track.getId(), 0)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            trackService.createTrack(track.getTitle(), track.getDescription(), track.getSubmitDeadline(),
                    track.getReviewDeadline(), track.getPaperType(), track.getEventId());
            return ResponseEntity.ok(track);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        }
    }

    /**
     * Updates an existing track account and saves it in the repository.
     *
     * @param track the updated track account to be saved.
     * @return the updated track that was saved.
     */
    @Override
    @Transactional
    public ResponseEntity<Void> updateTrack(@Valid @RequestBody(required = false) Track track) {
        try {
            if (!roleService.hasPermission(userService, authManager, attendeeService,
                    track.getEventId(), track.getId(), 1)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            trackService.updateTrack(track.getId(), track.getTitle(), track.getDescription(), track.getSubmitDeadline(),
                    track.getReviewDeadline(), track.getPaperType(), track.getEventId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Deletes the track with the specified id.
     *
     * @param trackId id of the to be deleted track
     * @return the deleted track
     */
    @Override
    @Transactional
    public ResponseEntity<Void> deleteTrack(@PathVariable("trackID") Integer trackId) {
        try {
            if (trackId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
            }
            if (!roleService.hasPermission(userService, authManager, attendeeService,
                    trackService.getTrackById(trackId.longValue()).getEvent().getId(), trackId.longValue(), 0)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            trackService.deleteTrackById(trackId.longValue());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Retrieves the track with the specified id.
     *
     * @param trackId id of a track
     * @return track with this id if exists, else null
     */
    @Override
    @Transactional
    public ResponseEntity<Track> getTrackByID(@PathVariable("trackId") Integer trackId) {
        try {
            if (!userService.userExistsByEmail(new Email(authManager.getEmail()))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            Track track = trackService.getTrackById(trackId.longValue()).toModelTrack();
            return ResponseEntity.ok(track); // 200
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Retrieves all tracks with the specified title.
     *
     * @param title     title of a track
     * @param eventId   parent event of a track
     * @param paperType paperType of a track requirment
     * @return tracks with this title if exists, else null
     */
    @Override
    @Transactional
    public ResponseEntity<List<Track>> getTrack(String title, Integer eventId, PaperType paperType) {
        try {
            if (!userService.userExistsByEmail(new Email(authManager.getEmail()))) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            List<Track> tracks;
            if (eventId != null && title != null) {
                Track track = trackService.getTrackByTitleInEvent(new Title(title), eventId.longValue()).toModelTrack();
                tracks = new ArrayList<>();
                tracks.add(track);
            } else if (title != null) {
                tracks = new ArrayList<>();
                trackService.getTrackByTitle(new Title(title)).forEach(t -> tracks.add(t.toModelTrack()));
            } else if (eventId != null) {
                tracks = new ArrayList<>();
                trackService.getTrackByParentEvent(eventId.longValue()).forEach(t -> tracks.add(t.toModelTrack()));
            } else {
                throw new IllegalArgumentException("Null reference for track title and event id.");
            }
            return ResponseEntity.ok(tracks); // 200
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

}
