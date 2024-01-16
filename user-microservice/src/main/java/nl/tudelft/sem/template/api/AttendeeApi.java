/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package nl.tudelft.sem.template.api;

import nl.tudelft.sem.template.model.Attendee;
import nl.tudelft.sem.template.model.Role;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-16T14:25:27.394477500+01:00[Europe/Amsterdam]")
@Validated
@Tag(name = "Event Management", description = "end-points for features that help users create and manage events when permitted by their assigned role.")
public interface AttendeeApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /attendee : Create a new attendee.
     * The invitation system should be used by human users of the API. This endpoint is only designed to be used by other microservices.
     *
     * @param attendee Attendee to be created. (optional)
     * @return Operation executed successfully (status code 200)
     *         or Unauthorized access. (status code 401)
     *         or Attendee already exists. (status code 409)
     */
    @Operation(
        operationId = "createAttendee",
        summary = "Create a new attendee.",
        description = "The invitation system should be used by human users of the API. This endpoint is only designed to be used by other microservices.",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Operation executed successfully", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Attendee.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "409", description = "Attendee already exists.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/attendee",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<Attendee> createAttendee(
        @Parameter(name = "Attendee", description = "Attendee to be created.") @Valid @RequestBody(required = false) Attendee attendee
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"event_id\" : 10, \"user_id\" : 10, \"track_id\" : 10, \"id\" : 10 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /attendee/{attendeeID} : Remove an attendee from a particular event or track.
     * This operation can only be performed by someone with an equal or higher role in the given event or track than the attendee&#39;s role.
     *
     * @param attendeeID  (required)
     * @return Successful operation (status code 200)
     *         or Invalid attendeeID was provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or The user specified with the userID never attended the event specified by the eventID. (status code 404)
     */
    @Operation(
        operationId = "deleteAttendee",
        summary = "Remove an attendee from a particular event or track.",
        description = "This operation can only be performed by someone with an equal or higher role in the given event or track than the attendee's role.",
        tags = { "Event Management", "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid attendeeID was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "The user specified with the userID never attended the event specified by the eventID.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/attendee/{attendeeID}"
    )
    default ResponseEntity<Void> deleteAttendee(
        @Parameter(name = "attendeeID", description = "", required = true, in = ParameterIn.PATH) @PathVariable("attendeeID") Long attendeeID
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /attendee/{attendeeID} : Get an attendee by ID.
     *
     * @param attendeeID  (required)
     * @return successful operation (status code 200)
     *         or Invalid attendee ID was provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or No attendee with the provided ID exists. (status code 404)
     */
    @Operation(
        operationId = "getAttendeeByID",
        summary = "Get an attendee by ID.",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Attendee.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid attendee ID was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "No attendee with the provided ID exists.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/attendee/{attendeeID}",
        produces = { "application/json" }
    )
    default ResponseEntity<Attendee> getAttendeeByID(
        @Parameter(name = "attendeeID", description = "", required = true, in = ParameterIn.PATH) @PathVariable("attendeeID") Long attendeeID
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"event_id\" : 10, \"user_id\" : 10, \"track_id\" : 10, \"id\" : 10 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /attendee : Returns all attendees, optionally filtered by an event, roles, or track.
     *
     * @param eventID Event identifier filter. (optional)
     * @param roles Role filters (optional)
     * @param trackID Track identifier filter (optional)
     * @return successful operation (status code 200)
     *         or Invalid filters were provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     */
    @Operation(
        operationId = "getInvitesByEventID",
        summary = "Returns all attendees, optionally filtered by an event, roles, or track.",
        tags = { "Event Management", "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Attendee.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid filters were provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/attendee",
        produces = { "application/json" }
    )
    default ResponseEntity<List<Attendee>> getInvitesByEventID(
        @Parameter(name = "eventID", description = "Event identifier filter.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "eventID", required = false) Long eventID,
        @Parameter(name = "roles", description = "Role filters", in = ParameterIn.QUERY) @Valid @RequestParam(value = "roles", required = false) List<Role> roles,
        @Parameter(name = "trackID", description = "Track identifier filter", in = ParameterIn.QUERY) @Valid @RequestParam(value = "trackID", required = false) Long trackID
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"event_id\" : 10, \"user_id\" : 10, \"track_id\" : 10, \"id\" : 10 }, { \"event_id\" : 10, \"user_id\" : 10, \"track_id\" : 10, \"id\" : 10 } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /attendee : Updates the role of an attendee in the specified event or track.
     * This operation can only be performed by someone with an equal or higher role in the given event or track than the attendee&#39;s current and target roles.
     *
     * @param attendee The attendee object to be updated. (optional)
     * @return successful operation (status code 200)
     *         or Invalid attendee object was provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or No attendee with the provided ID exists. (status code 404)
     */
    @Operation(
        operationId = "updateAttendee",
        summary = "Updates the role of an attendee in the specified event or track.",
        description = "This operation can only be performed by someone with an equal or higher role in the given event or track than the attendee's current and target roles.",
        tags = { "Event Management", "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Attendee.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid attendee object was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "No attendee with the provided ID exists.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/attendee",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<Attendee> updateAttendee(
        @Parameter(name = "Attendee", description = "The attendee object to be updated.") @Valid @RequestBody(required = false) Attendee attendee
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"event_id\" : 10, \"user_id\" : 10, \"track_id\" : 10, \"id\" : 10 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
