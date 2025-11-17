package com.theworkers.usermicroservice.controller;

import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.output.WebResponse;
import com.theworkers.usermicroservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create a User, with Request Body via POST, " +
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
    public WebResponse createUser(@Valid @RequestBody UserInputDTO newUser) {
        return userService.createUser(newUser);
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

}
