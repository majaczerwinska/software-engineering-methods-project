/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package nl.tudelft.sem.template.api;

import nl.tudelft.sem.template.model.PaperType;
import nl.tudelft.sem.template.model.Track;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-18T11:55:34.283573400+01:00[Europe/Berlin]")
@Validated
@Tag(name = "Track Management", description = "end-points that provide track-related functionalities.")
public interface TrackApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /track : Create a new track.
     * This operation can only be performed by the general chair of the related event.
     *
     * @param track Track object to be created. (optional)
     * @return successful operation. (status code 200)
     *         or Unauthorized access. (status code 401)
     *         or Invalid input for new track (status code 400)
     */
    @Operation(
        operationId = "addTrack",
        summary = "Create a new track.",
        description = "This operation can only be performed by the general chair of the related event.",
        tags = { "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Track.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "400", description = "Invalid input for new track")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/track",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<Track> addTrack(
        @Parameter(name = "Track", description = "Track object to be created.") @Valid @RequestBody(required = false) Track track
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"review_deadline\" : \"2000-01-23\", \"event_id\" : 5, \"submit_deadline\" : \"2000-01-23\", \"description\" : \"track 1 is ..., aims to ...\", \"id\" : 10, \"title\" : \"track Title 1\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /track/{trackID} : Delete a track.
     *
     * @param trackID ID of the track to be deleted. (required)
     * @return Track successfully deleted. (status code 204)
     *         or Invalid trackID supplied. (status code 400)
     *         or Unahtorized access. (status code 401)
     *         or Track not found. (status code 404)
     */
    @Operation(
        operationId = "deleteTrack",
        summary = "Delete a track.",
        tags = { "Track Management" },
        responses = {
            @ApiResponse(responseCode = "204", description = "Track successfully deleted."),
            @ApiResponse(responseCode = "400", description = "Invalid trackID supplied."),
            @ApiResponse(responseCode = "401", description = "Unahtorized access."),
            @ApiResponse(responseCode = "404", description = "Track not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/track/{trackID}"
    )
    default ResponseEntity<Void> deleteTrack(
        @Parameter(name = "trackID", description = "ID of the track to be deleted.", required = true, in = ParameterIn.PATH) @PathVariable("trackID") Integer trackID
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /track : Gets tracks
     *
     * @param name Track&#39;s name. (optional)
     * @param eventID Event to which the track belongs. (optional)
     * @param paperType Track&#39;s paper type (optional)
     * @return successful operation (status code 200)
     *         or Invalid query values. (status code 400)
     *         or Unauthorized access. (status code 401)
     */
    @Operation(
        operationId = "getTrack",
        summary = "Gets tracks",
        tags = { "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Track.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid query values."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/track",
        produces = { "application/json" }
    )
    default ResponseEntity<List<Track>> getTrack(
        @Parameter(name = "name", description = "Track's name.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "name", required = false) String name,
        @Parameter(name = "eventID", description = "Event to which the track belongs.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "eventID", required = false) Integer eventID,
        @Parameter(name = "paper_type", description = "Track's paper type", in = ParameterIn.QUERY) @Valid @RequestParam(value = "paper_type", required = false) PaperType paperType
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"review_deadline\" : \"2000-01-23\", \"event_id\" : 5, \"submit_deadline\" : \"2000-01-23\", \"description\" : \"track 1 is ..., aims to ...\", \"id\" : 10, \"title\" : \"track Title 1\" }, { \"review_deadline\" : \"2000-01-23\", \"event_id\" : 5, \"submit_deadline\" : \"2000-01-23\", \"description\" : \"track 1 is ..., aims to ...\", \"id\" : 10, \"title\" : \"track Title 1\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /track/{trackID} : Get track by ID
     * This operation can only be performed by the general chair of the related event.
     *
     * @param trackID ID of the track to be returned. (required)
     * @return successful operation. (status code 200)
     *         or Invalid TrackID supplied (status code 400)
     *         or Unahtorized access. (status code 401)
     *         or Track not found (status code 404)
     */
    @Operation(
        operationId = "getTrackByID",
        summary = "Get track by ID",
        description = "This operation can only be performed by the general chair of the related event.",
        tags = { "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Track.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid TrackID supplied"),
            @ApiResponse(responseCode = "401", description = "Unahtorized access."),
            @ApiResponse(responseCode = "404", description = "Track not found")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/track/{trackID}",
        produces = { "application/json" }
    )
    default ResponseEntity<Track> getTrackByID(
        @Parameter(name = "trackID", description = "ID of the track to be returned.", required = true, in = ParameterIn.PATH) @PathVariable("trackID") Integer trackID
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"review_deadline\" : \"2000-01-23\", \"event_id\" : 5, \"submit_deadline\" : \"2000-01-23\", \"description\" : \"track 1 is ..., aims to ...\", \"id\" : 10, \"title\" : \"track Title 1\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /track : Update a track.
     * This operation can only be performed by the track&#39;s PC Chair, or the general chair of the associated event.
     *
     * @param track Track data to be changed. (optional)
     * @return Track successfully updated. (status code 204)
     *         or Invalid Track object supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or Track object not found. (status code 404)
     */
    @Operation(
        operationId = "updateTrack",
        summary = "Update a track.",
        description = "This operation can only be performed by the track's PC Chair, or the general chair of the associated event.",
        tags = { "Track Management" },
        responses = {
            @ApiResponse(responseCode = "204", description = "Track successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid Track object supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "Track object not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/track",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> updateTrack(
        @Parameter(name = "Track", description = "Track data to be changed.") @Valid @RequestBody(required = false) Track track
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
