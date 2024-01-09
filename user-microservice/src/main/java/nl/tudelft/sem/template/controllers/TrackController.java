package nl.tudelft.sem.template.controllers;


import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import nl.tudelft.sem.template.api.TrackApi;
import nl.tudelft.sem.template.domain.track.Title;
import nl.tudelft.sem.template.domain.track.Track;
import nl.tudelft.sem.template.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


//The redundancy of literals here is justified for the queries and the parameter alias.
/**
 * This controller is responsible for methods related to the Track entity.
 *
 */
@RestController
public class TrackController implements TrackApi {

    private final transient TrackService trackService;

    /**
     * Instantiates a new Track controller.
     *
     * @param trackService used to manage Track services
     */
    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    /**
     * Create a new Track.
     *
     * @param track - the RequestBody to create a new Track
     * @return ResponseEntity of new Track or an error response
     */
    // TODO: THE RETURN TYPE IS EITHER AN ERROR MASSAGE OR THE ADDED TRACK
    @PostMapping("/track")
    @PreAuthorize("hasRole('GENERAL_CHAIR')")
    public ResponseEntity<?> addTrack(@Valid @RequestBody(required = false) Track track) {
        try {
            // TODO: Check authorization (GENERAL_CHAIR)
            Track createdTrack = trackService.createTrack(track);
            return ResponseEntity.ok(createdTrack);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid track data: " + e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body("Unauthorized access for create new track: " + e.getMessage());
        }
    }

    /**
     * Updates an existing track account and saves it in the repository.
     *
     * @param track - the updated track account to be saved.
     * @return the updated track that was saved.
     */
    // TODO: THE RETURN TYPE IS EITHER A MASSAGE OR THE UPDATED TRACK
    @PutMapping("/track")
    @PreAuthorize("hasRole('PC_CHAIR') or hasRole('GENERAL_CHAIR')")
    public ResponseEntity<?> updateTrack(@Valid @RequestBody(required = false) Track track) {
        try {

            // TODO: Check authorization (PC_CHAIR or GENERAL_CHAIR)
            Track updatedTrack = trackService.updateTrack(track);
            return ResponseEntity.ok(updatedTrack);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body("Unauthorized access for update track: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid track data: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Track object not found: " + e.getMessage());
        }
    }

    /**
     * Deletes the track with the specified id.
     *
     * @param trackId - id of the to be deleted track
     * @return - the deleted track
     */
    // TODO: THE RETURN TYPE IS EITHER A MASSAGE OR THE DELETED TRACK
    @DeleteMapping("/track/{trackId}")
    @PreAuthorize("hasRole('PC_CHAIR') or hasRole('GENERAL_CHAIR')")
    public ResponseEntity<?> deleteTrack(@PathVariable("trackID") Long trackId) {
        try {
            // TODO: Check authorization (PC_CHAIR or GENERAL_CHAIR)
            Track deletedTrack = trackService.deleteTrackById(trackId);
            return ResponseEntity.ok(deletedTrack);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body("Unauthorized access for delete track: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid trackID supplied: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Track object not found: " + e.getMessage());
        }
    }

    /**
     * Retrieves the track with the specified id.
     *
     * @param trackId - id of a track
     * @return - track with this id if exists, else null
     */
    // TODO: THE RETURN TYPE IS EITHER A MASSAGE OR THE FOUNDED TRACK
    @GetMapping("/track/{trackId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTrackById(@PathVariable("trackId") Long trackId) {
        try {
            // TODO: check if user is authenticated
            Track track = trackService.getTrackById(trackId);
            return ResponseEntity.ok(track);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body("Unauthorized access for get track by id: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid trackId supplied: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Track object not found: " + e.getMessage());
        }
    }

    /**
     * Retrieves all tracks with the specified title.
     *
     * @param title - title of a track
     * @return - tracks with this title if exists, else null
     */
    // TODO: THE RETURN TYPE IS EITHER A MASSAGE OR THE FOUNDED TRACKS
    @GetMapping("/track/title/{title}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTrackByTitle(@PathVariable("title") String title) {
        try {
            // TODO: check if user is authenticated
            List<Track> tracks = trackService.getTrackByTitle(new Title(title));
            return ResponseEntity.ok(tracks);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body("Unauthorized access for get track by title: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid track title supplied: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("No Track objects was found: " + e.getMessage());
        }
    }

    /**
     * Retrieves all tracks within the specified event.
     *
     * @param parentEventId - parent event of a track
     * @return - tracks within this event if exists, else null
     */
    // TODO: THE RETURN TYPE IS EITHER A MASSAGE OR THE FOUNDED TRACKS
    @GetMapping("/track/event")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTrackByParentEvent(@RequestBody(required = false) Long parentEventId) {
        try {
            // TODO: check if user is authenticated
            List<Track> tracks = trackService.getTrackByParentEvent(parentEventId);
            return ResponseEntity.ok(tracks);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(401).body("Unauthorized access for get track by event: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid event supplied: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("No Track objects was found: " + e.getMessage());
        }
    }

}