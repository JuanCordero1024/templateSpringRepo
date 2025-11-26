package com.theworkers.usermicroservice.controller;

import com.theworkers.usermicroservice.model.enums.UserStatus;
import com.theworkers.usermicroservice.model.input.*;
import com.theworkers.usermicroservice.model.output.WebResponse;
import com.theworkers.usermicroservice.model.output.WebResponsePagination;
import com.theworkers.usermicroservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create a User, with Request Body via POST, " +
                    "receiving a User Model retrieving the user Model inserted",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    public WebResponse createUser(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authorization,
                                    @Valid @RequestBody UserInputDTO newUser) {
        log.info("Creating a User, with Request Body via POST: " +  newUser.toString());
        return userService.createUser(authorization, newUser);
    }

    @PostMapping("/verify-credentials")
    @Operation(
            summary = "Verify the crendentials of one User, with Request Body via POST, " +
                    "receiving a User Model retrieving the user Model inserted"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    public WebResponse verifyCredentials(@Valid @RequestBody UserLoginInput loginCredentials) {
        return userService.verifyUserCredentials(loginCredentials);
    }

    @PostMapping("/setEccToken/{userId}")
    @Operation(
            summary = "Verify the crendentials of one User, with Request Body via POST, " +
                    "receiving a User Model retrieving the user Model inserted"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    public WebResponse setECCToken(@Valid @RequestBody EccInputDTO eccInputDTO, @PathVariable("userId") UUID userId) {
        return userService.setECCToken(userId, eccInputDTO.getPublicKey());
    }

    @GetMapping("/read/{userId}")
    @Operation(
            summary = "Get one User, with Request Body via GET, " +
                    "receiving a User UUID retrieving the user Model"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    public WebResponse getUser(@Valid @PathVariable("userId") UUID userId) {
        return userService.readUser(userId);
    }

    @GetMapping("/read/for/sign/{userId}")
    @Operation(
            summary = "Get one User, with Request Body via GET, " +
                    "receiving a User UUID retrieving the user Model"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public WebResponse getUserForSign(@Parameter(hidden = true)
                                          @RequestHeader("Authorization") String authorization,
            @Valid @PathVariable("userId") UUID userId) {
        return userService.readUserForSign(userId);
    }

    @GetMapping("/get/AllUsers")
    @Operation(
            summary = "Retrieving all the users by the following filters: mail, role",
            parameters = {
                    @Parameter(name = "roleId", description = "Filtrar por Role ID", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
                    @Parameter(
                            name = "status",
                            description = "Filtrar por Status",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = UserStatus.class)
                    ),
                    @Parameter(name = "email", description = "Filtrar por Email", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
            }
            )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WebResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public WebResponsePagination getAllUsers(@RequestParam int page, @RequestParam int size,
                                             @Parameter(hidden = true)
                                             @RequestParam Map<String, String> filter)
    {
        filter.remove("page");
        filter.remove("size");
        return userService.readUsers(page, size, filter);
    }

    @Transactional
    @PutMapping("/update/{userId}")
    @Operation(
            summary = "Update a User, with Path Variable Id in the URL, " +
                    "receiving a Id and try to retrieving the user updated"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public WebResponse updateUser(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authorization,
            @PathVariable("userId") UUID userId,
            @RequestBody UserUpdateInputDTO data
    ) {
        return userService.udpateUser(authorization,data, userId);
    }

    @DeleteMapping("/delete/{userId}/{status}")
    @Operation(
            summary = "Delete a User, with Path Variable Id in the URL, " +
                    "receiving a Id and try to retrieving a simple message"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content
                    ),
            }
    )
    public WebResponse deleteUser(@PathVariable("userId") UUID id, @PathVariable("status") UserStatus status) {
        return userService.deleteUser(id, status);
    }


}
