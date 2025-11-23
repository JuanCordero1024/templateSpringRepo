package com.theworkers.rolemicroservice.controller;

import com.theworkers.rolemicroservice.model.input.RoleInputDTO;
import com.theworkers.rolemicroservice.model.output.RoleOutputDTO;
import com.theworkers.rolemicroservice.model.output.WebResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.theworkers.rolemicroservice.service.RoleService;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create a Role, with Request Body via POST, " +
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
    public WebResponse createRole(@Valid @RequestBody RoleInputDTO newRole) {
        return roleService.createRole(newRole);
    }

    @GetMapping("/read/{nameRole}")
    @Operation(
            summary = "Read a Rolen, with Request Body via GET, " +
                    "receiving a Name of Role"
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
    public WebResponse readRole(@Valid @RequestParam("nameRole") String roleName) {
        return roleService.readRole(roleName);
    }

    @GetMapping("/readById")
    @Operation(
            summary = "Read a Rolen, with Request Body via GET, " +
                    "receiving a Name of Role"
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
    public RoleOutputDTO readRoleById(@RequestParam("roleId") Long roleId) {
        return roleService.readRoleById(roleId);
    }


}
