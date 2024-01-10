/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.6.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package nl.tudelft.sem.template.api;

import nl.tudelft.sem.template.model.User;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-01-11T00:54:22.581055+01:00[Europe/Amsterdam]")
@Validated
@Tag(name = "User Account Management", description = "end-points for operations that deal with account registration, account deletion, and account information modifications.")
public interface UserApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /user : Create a new user account.
     * This operation can only be performed by the holder of a token with the same email as that which the new user will have.
     *
     * @param user User to be created. (optional)
     * @return Operation executed successfully (status code 200)
     *         or Unauthorized access. (status code 401)
     *         or User already exists. (status code 409)
     */
    @Operation(
        operationId = "createAccount",
        summary = "Create a new user account.",
        description = "This operation can only be performed by the holder of a token with the same email as that which the new user will have.",
        tags = { "User Account Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Operation executed successfully", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "409", description = "User already exists.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/user",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<User> createAccount(
        @Parameter(name = "User", description = "User to be created.") @Valid @RequestBody(required = false) User user
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"Marieke\", \"lastName\" : \"Smith\", \"preferredCommunication\" : \"e-mail\", \"affiliation\" : \"Fireman\", \"personalWebsite\" : \"myPersonalWebsite.com\", \"id\" : 10, \"email\" : \"pete@email.com\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * DELETE /user/{userID} : Delete a user account.
     * This operation can only be performed by the holder of a token with the same email as that which the user being deleted has.
     *
     * @param userID The userID of the user that should be deleted. (required)
     * @return User successfully deleted. (status code 204)
     *         or Invalid userID supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or User not found. (status code 404)
     */
    @Operation(
        operationId = "deleteAccount",
        summary = "Delete a user account.",
        description = "This operation can only be performed by the holder of a token with the same email as that which the user being deleted has.",
        tags = { "User Account Management" },
        responses = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted."),
            @ApiResponse(responseCode = "400", description = "Invalid userID supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "User not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/user/{userID}"
    )
    default ResponseEntity<Void> deleteAccount(
        @Parameter(name = "userID", description = "The userID of the user that should be deleted.", required = true, in = ParameterIn.PATH) @PathVariable("userID") Integer userID
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /user/byEmail/{email} : Get an account by email
     *
     * @param email  (required)
     * @return successful operation (status code 200)
     *         or Invalid email supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or User not found. (status code 404)
     */
    @Operation(
        operationId = "getAccountByEmail",
        summary = "Get an account by email",
        tags = { "User Account Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid email supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "User not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/user/byEmail/{email}",
        produces = { "application/json" }
    )
    default ResponseEntity<User> getAccountByEmail(
        @Parameter(name = "email", description = "", required = true, in = ParameterIn.PATH) @PathVariable("email") String email
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"Marieke\", \"lastName\" : \"Smith\", \"preferredCommunication\" : \"e-mail\", \"affiliation\" : \"Fireman\", \"personalWebsite\" : \"myPersonalWebsite.com\", \"id\" : 10, \"email\" : \"pete@email.com\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /user/{userID} : Get a user account by userID.
     *
     * @param userID The userID of the user account. (required)
     * @return successful operation (status code 200)
     *         or Invalid userID supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or User not found. (status code 404)
     */
    @Operation(
        operationId = "getAccountByID",
        summary = "Get a user account by userID.",
        tags = { "User Account Management" },
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid userID supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "User not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/user/{userID}",
        produces = { "application/json" }
    )
    default ResponseEntity<User> getAccountByID(
        @Parameter(name = "userID", description = "The userID of the user account.", required = true, in = ParameterIn.PATH) @PathVariable("userID") Integer userID
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"Marieke\", \"lastName\" : \"Smith\", \"preferredCommunication\" : \"e-mail\", \"affiliation\" : \"Fireman\", \"personalWebsite\" : \"myPersonalWebsite.com\", \"id\" : 10, \"email\" : \"pete@email.com\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /user : Update a user&#39;s account information.
     * This operation can only be performed by the holder of a token with the same email as that which the user being updated has.
     *
     * @param user User information to be updated. (optional)
     * @return User account successfully updated (status code 204)
     *         or Invalid User object supplied. (status code 400)
     *         or Unauthorized access. (status code 401)
     *         or User not found. (status code 404)
     */
    @Operation(
        operationId = "updateAccount",
        summary = "Update a user's account information.",
        description = "This operation can only be performed by the holder of a token with the same email as that which the user being updated has.",
        tags = { "User Account Management" },
        responses = {
            @ApiResponse(responseCode = "204", description = "User account successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid User object supplied."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access."),
            @ApiResponse(responseCode = "404", description = "User not found.")
        },
        security = {
            @SecurityRequirement(name = "api_key")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/user",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> updateAccount(
        @Parameter(name = "User", description = "User information to be updated.") @Valid @RequestBody(required = false) User user
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
