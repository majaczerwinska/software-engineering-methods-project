/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package nl.tudelft.sem.template.api;

import nl.tudelft.sem.template.model.Attendee;
import nl.tudelft.sem.template.model.Invitation;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-11T14:29:55.907856+01:00[Europe/Amsterdam]")
@Validated
@Tag(name = "User-Invitation Interaction", description = "end-points that facilitate a user's interaction with invitations to events.")
public interface InvitationsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /invitations/accept/{invitationID} : Accept an invitation.
     * On success, this creates a new attendee for the event or track.
     *
     * @param invitationID ID of the invitation to be accepted. (required)
     * @return successful operation. (status code 200)
     *         or Invalid invitationID was provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or No invitation with the provided ID exists. (status code 404)
     */
    @Operation(
        operationId = "acceptInvitation",
        summary = "Accept an invitation.",
        description = "On success, this creates a new attendee for the event or track.",
        tags = { "User-Invitation Interaction" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Attendee.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid invitationID was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "No invitation with the provided ID exists.")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/invitations/accept/{invitationID}",
        produces = { "application/json" }
    )
    default ResponseEntity<Attendee> acceptInvitation(
        @Parameter(name = "invitationID", description = "ID of the invitation to be accepted.", required = true, in = ParameterIn.PATH) @PathVariable("invitationID") Integer invitationID
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
     * POST /invitations : Create a new invitation.
     * This operation can only be performed by a user with a role in the track or event that is higher than or equal to the role of the invited user.
     *
     * @param invitation The invitation object to be created. (optional)
     * @return Successful operation. (status code 200)
     *         or Invalid Invitation object was provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or Invitation already exists. (status code 409)
     */
    @Operation(
        operationId = "createInvitation",
        summary = "Create a new invitation.",
        description = "This operation can only be performed by a user with a role in the track or event that is higher than or equal to the role of the invited user.",
        tags = { "Event Management", "Track Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Invitation.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid Invitation object was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "409", description = "Invitation already exists.")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/invitations",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<Invitation> createInvitation(
        @Parameter(name = "Invitation", description = "The invitation object to be created.") @Valid @RequestBody(required = false) Invitation invitation
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
     * DELETE /invitations/{invitationID} : Reject or revoke an invitation.
     * This operation can only be performed by the invited user, or a user with a role in the event or track that is at least as high as the role to which the user has been invited.
     *
     * @param invitationID ID of the invitation to be deleted. (required)
     * @return successful operation. (status code 204)
     *         or Invalid invitationID was provided. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or No invitation with the provided ID exists. (status code 404)
     */
    @Operation(
        operationId = "deleteInvitation",
        summary = "Reject or revoke an invitation.",
        description = "This operation can only be performed by the invited user, or a user with a role in the event or track that is at least as high as the role to which the user has been invited.",
        tags = { "User-Invitation Interaction" },
        responses = {
            @ApiResponse(responseCode = "204", description = "successful operation."),
            @ApiResponse(responseCode = "400", description = "Invalid invitationID was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "No invitation with the provided ID exists.")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/invitations/{invitationID}"
    )
    default ResponseEntity<Void> deleteInvitation(
        @Parameter(name = "invitationID", description = "ID of the invitation to be deleted.", required = true, in = ParameterIn.PATH) @PathVariable("invitationID") Integer invitationID
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /invitations/{invitationID} : Get an invitation by ID.
     *
     * @param invitationID ID of the invitation to be retrieved. (required)
     * @return successful operation. (status code 200)
     *         or Invalid invitationID was provided. (status code 400)
     *         or Unauthorized Access. (status code 401)
     *         or No invitation with the provided ID exists. (status code 404)
     */
    @Operation(
        operationId = "getInvitation",
        summary = "Get an invitation by ID.",
        tags = { "User-Invitation Interaction" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation."),
            @ApiResponse(responseCode = "400", description = "Invalid invitationID was provided."),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access."),
            @ApiResponse(responseCode = "404", description = "No invitation with the provided ID exists.")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/invitations/{invitationID}"
    )
    default ResponseEntity<Void> getInvitation(
        @Parameter(name = "invitationID", description = "ID of the invitation to be retrieved.", required = true, in = ParameterIn.PATH) @PathVariable("invitationID") Integer invitationID
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /invitations : Retrieve all invitations matching a userID, trackID and/or eventID.
     *
     * @param userID The ID of the user. (optional)
     * @param trackID The ID of the track. (optional)
     * @param eventID The ID of the event. (optional)
     * @return Successful operation. (status code 200)
     *         or Invalid parameters were specified. (status code 400)
     *         or Unauthorized Access. (status code 401)
     */
    @Operation(
        operationId = "getInvitations",
        summary = "Retrieve all invitations matching a userID, trackID and/or eventID.",
        tags = { "Track Management", "Event Management", "User-Invitation Interaction" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation.", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Invitation.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters were specified."),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access.")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/invitations",
        produces = { "application/json" }
    )
    default ResponseEntity<List<Invitation>> getInvitations(
        @Parameter(name = "userID", description = "The ID of the user.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "userID", required = false) Long userID,
        @Parameter(name = "trackID", description = "The ID of the track.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "trackID", required = false) Long trackID,
        @Parameter(name = "eventID", description = "The ID of the event.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "eventID", required = false) Long eventID
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

}
