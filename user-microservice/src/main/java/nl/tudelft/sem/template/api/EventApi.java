/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package nl.tudelft.sem.template.api;

import org.springframework.format.annotation.DateTimeFormat;
import nl.tudelft.sem.template.model.Event;
import java.time.LocalDate;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-17T18:08:56.367967600+01:00[Europe/Berlin]")
@Validated
@Tag(name = "Event Management", description = "end-points for features that help users create and manage events when permitted by their assigned role.")
public interface EventApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /event : Create a new event.
     * Upon creation, the user associated with the provided token will be designated the general chair of the event.
     *
     * @param event Event to be created. (required)
     * @return Successful operation. (status code 200)
     *         or Unauthorized access. (status code 401)
     *         or Invalid input. (status code 400)
     */
    @Operation(
        operationId = "addEvent",
        summary = "Create a new event.",
        description = "Upon creation, the user associated with the provided token will be designated the general chair of the event.",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/event",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<Event> addEvent(
        @Parameter(name = "Event", description = "Event to be created.", required = true) @Valid @RequestBody Event event
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"end_date\" : \"2000-01-23\", \"name\" : \"Open conference for paper review\", \"description\" : \"this is a conference that deals with submitting papers to review.\", \"id\" : 4321234, \"is_cancelled\" : true, \"start_date\" : \"2000-01-23\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /event/{eventID} : Delete an event.
     * This operation can only be performed by the general chair of the event.
     *
     * @param eventID ID of event to be deleted. (required)
     * @return successful operation. (status code 204)
     *         or Invalid eventID. (status code 400)
     *         or Unauthorized access. (status code 401)
     */
    @Operation(
        operationId = "deleteEvent",
        summary = "Delete an event.",
        description = "This operation can only be performed by the general chair of the event.",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "204", description = "successful operation."),
            @ApiResponse(responseCode = "400", description = "Invalid eventID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/event/{eventID}"
    )
    default ResponseEntity<Void> deleteEvent(
        @Parameter(name = "eventID", description = "ID of event to be deleted.", required = true, in = ParameterIn.PATH) @PathVariable("eventID") Long eventID
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /event : Finds Events
     * Finds Events
     *
     * @param startBefore Date before which event starts (optional)
     * @param startAfter Date after which event starts (optional)
     * @param endBefore Date before which event ends (optional)
     * @param endAfter Date after which event ends (optional)
     * @param cancelled Whether the event is cancelled (optional, default to false)
     * @param name Event&#39;s name. (optional)
     * @return successful operation (status code 200)
     *         or Invalid query values. (status code 400)
     *         or Unauthorized access. (status code 401)
     */
    @Operation(
        operationId = "findEvent",
        summary = "Finds Events",
        description = "Finds Events",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Event.class)))
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
        value = "/event",
        produces = { "application/json" }
    )
    default ResponseEntity<List<Event>> findEvent(
        @Parameter(name = "start_before", description = "Date before which event starts", in = ParameterIn.QUERY) @Valid @RequestParam(value = "start_before", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startBefore,
        @Parameter(name = "start_after", description = "Date after which event starts", in = ParameterIn.QUERY) @Valid @RequestParam(value = "start_after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startAfter,
        @Parameter(name = "end_before", description = "Date before which event ends", in = ParameterIn.QUERY) @Valid @RequestParam(value = "end_before", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endBefore,
        @Parameter(name = "end_after", description = "Date after which event ends", in = ParameterIn.QUERY) @Valid @RequestParam(value = "end_after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endAfter,
        @Parameter(name = "cancelled", description = "Whether the event is cancelled", in = ParameterIn.QUERY) @Valid @RequestParam(value = "cancelled", required = false, defaultValue = "false") Boolean cancelled,
        @Parameter(name = "name", description = "Event's name.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "name", required = false) String name
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ { \"end_date\" : \"2000-01-23\", \"name\" : \"Open conference for paper review\", \"description\" : \"this is a conference that deals with submitting papers to review.\", \"id\" : 4321234, \"is_cancelled\" : true, \"start_date\" : \"2000-01-23\" }, { \"end_date\" : \"2000-01-23\", \"name\" : \"Open conference for paper review\", \"description\" : \"this is a conference that deals with submitting papers to review.\", \"id\" : 4321234, \"is_cancelled\" : true, \"start_date\" : \"2000-01-23\" } ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /event/{eventID} : Get an event by eventID.
     *
     * @param eventID ID of event to return. (required)
     * @return successful operation (status code 200)
     *         or Invalid ID supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or Event not found. (status code 404)
     */
    @Operation(
        operationId = "getEventById",
        summary = "Get an event by eventID.",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "Event not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/event/{eventID}",
        produces = { "application/json" }
    )
    default ResponseEntity<Event> getEventById(
        @Parameter(name = "eventID", description = "ID of event to return.", required = true, in = ParameterIn.PATH) @PathVariable("eventID") Long eventID
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"end_date\" : \"2000-01-23\", \"name\" : \"Open conference for paper review\", \"description\" : \"this is a conference that deals with submitting papers to review.\", \"id\" : 4321234, \"is_cancelled\" : true, \"start_date\" : \"2000-01-23\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /event : Update an existing event.
     * This operation can only be performed by the general chair of the event.
     *
     * @param event Event data to be changed. (required)
     * @return Successful operation. (status code 200)
     *         or Invalid event object supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or Event not found. (status code 404)
     */
    @Operation(
        operationId = "updateEvent",
        summary = "Update an existing event.",
        description = "This operation can only be performed by the general chair of the event.",
        tags = { "Event Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid event object supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "Event not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/event",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<Event> updateEvent(
        @Parameter(name = "Event", description = "Event data to be changed.", required = true) @Valid @RequestBody Event event
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"end_date\" : \"2000-01-23\", \"name\" : \"Open conference for paper review\", \"description\" : \"this is a conference that deals with submitting papers to review.\", \"id\" : 4321234, \"is_cancelled\" : true, \"start_date\" : \"2000-01-23\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
