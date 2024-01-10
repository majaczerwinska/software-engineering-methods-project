package nl.tudelft.sem.template.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import nl.tudelft.sem.template.api.TrackApi;
import nl.tudelft.sem.template.authentication.AuthManager;
import nl.tudelft.sem.template.domain.attendee.Attendee;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.model.Track;
import nl.tudelft.sem.template.services.AttendeeService;
import nl.tudelft.sem.template.services.TrackService;
import nl.tudelft.sem.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//The redundancy of literals here is justified for the queries and the parameter alias.

/**
 * This controller is responsible for methods related to the Track entity.
 */
@RestController
public class TrackController implements TrackApi {

    private final transient AuthManager authManager;
    private final transient AttendeeService attendeeService;
    private final transient TrackService trackService;
    private final transient UserService userService;

    /**
     * Instantiates a new Track controller.
     *
     * @param trackService used to manage Track services
     * @param authManager  Spring Security component used to authenticate and
     *                     authorize the user
     */
    @Autowired
    public TrackController(AuthManager authManager, AttendeeService attendeeService,
                           TrackService trackService, UserService userService) {
        this.authManager = authManager;
        this.attendeeService = attendeeService;
        this.trackService = trackService;
        this.userService = userService;
    }


    /**
     * Create a new Track.
     *
     * @param track the RequestBody to create a new Track
     * @return ResponseEntity of new Track or an error response
     */
    @Override
    @Transactional
    //    @PreAuthorize("@RoleService.isGeneralChairForEvent("
    //            + "userService.getUserByEmail(new Email(authManager.getEmail())),"
    //            + " #track.getEventId())")
    public ResponseEntity<Track> addTrack(Track track) {
        // Check authorization: user is (GENERAL_CHAIR) of the event
        try {
            AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
            if (user == null) {
                throw new AccessDeniedException("User not found.");
            }
            Attendee role = attendeeService.getAttendance(user.getId(), track.getEventId(), null);
            if (!(role.isConfirmed() && role.getRole().getRoleTitle().getPermission() <= 0)) {
                throw new AccessDeniedException("User do not have right to add track.");
            }

            trackService.createTrack(new nl.tudelft.sem.template.domain.track.Track(track));
            return ResponseEntity.ok(track);
        } catch (AccessDeniedException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    //    @PreAuthorize("hasRole('PC_CHAIR') or hasRole('GENERAL_CHAIR')")
    public ResponseEntity<Void> updateTrack(@Valid @RequestBody(required = false) Track track) {
        try {
            AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
            if (user == null) {
                throw new AccessDeniedException("User not found.");
            }
            Attendee role = attendeeService.getAttendance(user.getId(), track.getEventId(), null);
            if (!(role.isConfirmed() && role.getRole().getRoleTitle().getPermission() <= 1)) {
                throw new AccessDeniedException("User do not have right to add track.");
            }

            trackService.updateTrack(new nl.tudelft.sem.template.domain.track.Track(track));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
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
    //    @PreAuthorize("hasRole('PC_CHAIR') or hasRole('GENERAL_CHAIR')")
    public ResponseEntity<Void> deleteTrack(@PathVariable("trackID") Integer trackId) {
        try {
            Track track = trackService.getTrackById(trackId).toModelTrack();
            AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
            if (user == null) {
                throw new AccessDeniedException("User not found.");
            }
            Attendee role = attendeeService.getAttendance(user.getId(), track.getEventId(), null);
            if (!(role.isConfirmed() && role.getRole().getRoleTitle().getPermission() <= 1)) {
                throw new AccessDeniedException("User do not have right to add track.");
            }

            trackService.deleteTrackById(trackId.longValue());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
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
    //    @PreAuthorize("@RoleService.isUser(userService.getUserByEmail(new Email(authManager.getEmail())))")
    public ResponseEntity<Track> getTrackByID(@PathVariable("trackId") Integer trackId) {
        try {
            AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
            if (user == null) {
                throw new AccessDeniedException("User not found.");
            }

            Track track = trackService.getTrackById(trackId.longValue()).toModelTrack();
            return ResponseEntity.ok(track); // 200
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    /**
     * Retrieves all tracks with the specified title.
     *
     * @param title   title of a track
     * @param eventId parent event of a track
     * @return tracks with this title if exists, else null
     */
    @Override
    public ResponseEntity<List<Track>> getTrack(@Nullable String title,
                                                @Nullable Integer eventId, @Nullable PaperType paperType) {
        try {
            AppUser user = userService.getUserByEmail(new Email(authManager.getEmail()));
            if (user == null) {
                throw new AccessDeniedException("User not found.");
            }

            List<Track> tracks;
            if (eventId != null && title != null) {
                Track track = trackService.getTrackByTitleInEvent(new Title(title), eventId.longValue()).toModelTrack();
                tracks = new ArrayList<>();
                tracks.add(track);
                return ResponseEntity.ok(tracks); // 200
            } else if (title != null) {
                tracks = new ArrayList<>();
                trackService.getTrackByTitle(new Title(title)).forEach(t -> tracks.add(t.toModelTrack()));
                return ResponseEntity.ok(tracks); // 200
            } else if (eventId != null) {
                tracks = new ArrayList<>();
                trackService.getTrackByParentEvent(eventId.longValue()).forEach(t -> tracks.add(t.toModelTrack()));
                return ResponseEntity.ok(tracks); // 200
            } else {
                throw new IllegalArgumentException("Null reference for track title and event id.");
            }

        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

}