package com.theworkers.templatemicroservice.controller;

import com.theworkers.templatemicroservice.model.enums.FileCategory;
import com.theworkers.templatemicroservice.model.enums.FileStatus;
import com.theworkers.templatemicroservice.model.input.UserFileInputDTO;
import com.theworkers.templatemicroservice.model.input.UserFileSignInputDTO;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import com.theworkers.templatemicroservice.model.output.WebResponsePagination;
import com.theworkers.templatemicroservice.service.UserFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/file")
public class UserFileController {

    private final UserFileService userFileService;
    public UserFileController(UserFileService userFileService) {
        this.userFileService = userFileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a File with Metadata",
            description = "Receives a binary file and a JSON object with metadata (UserId, Category, etc.) via Multipart request."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo subido y guardado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos o archivo vacío",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno al escribir en disco",
                            content = @Content
                    ),
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public WebResponse uploadFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String bearerToken,
            @Parameter(description = "Select the file to upload", required = true)
            @RequestPart("file") MultipartFile file,

            @Parameter(description = "User ID", required = true)
            @RequestParam("userId") UUID userId,

            @Parameter(description = "File name", required = true)
            @RequestParam("fileName") String fileName,

            @Parameter(description = "Category", required = true)
            @RequestParam("category") FileCategory category
    ) {
        UserFileInputDTO dto = new UserFileInputDTO();
        dto.setUserId(userId);
        dto.setFileName(fileName);
        dto.setFileCategory(category);
        return userFileService.uploadFile(bearerToken,dto, file);
    }

    @GetMapping(value = "/download/{userFileId}/{status}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Descargar archivo", description = "Recupera y descarga el archivo físico guardado mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Archivo no encontrado en base de datos o disco", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la descarga", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String bearerToken,
            @Parameter(description = "ID único del archivo (UUID)", required = true)
            @PathVariable("userFileId") UUID userFileId, @PathVariable ("status") FileStatus status
    ) {
        return userFileService.downloadFile(bearerToken,userFileId, status);
    }

    @GetMapping(value = "/preview/{userFileId}/{status}", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Descargar archivo", description = "Recupera y descarga el archivo físico guardado mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_PDF_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Archivo no encontrado en base de datos o disco", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la descarga", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Resource> previewFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String bearerToken,
            @Parameter(description = "ID único del archivo (UUID)", required = true)
            @PathVariable("userFileId") UUID userFileId, @PathVariable ("status") FileStatus status
    ) {
        return userFileService.previewFile(bearerToken,userFileId, status);
    }

    @PostMapping(value = "/sign", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Firmar digitalmente un documento",
            description = "Toma un archivo previamente subido (por ID), recupera la llave privada del usuario, y solicita al microservicio de Python que aplique marca de agua y firma criptográfica."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documento firmado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Archivo original o usuario no encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error en el proceso de firma (Python o Disco)",
                            content = @Content
                    ),
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public WebResponse signFile(
            @Parameter(hidden = true) @RequestHeader("Authorization") String bearerToken,
            @Parameter(description = "DTO con IDs del usuario y del documento", required = true)
            @Valid @RequestBody UserFileSignInputDTO signRequest
    ) {
        return userFileService.signFile(bearerToken,signRequest);
    }

    @GetMapping("/get/AllDocuments")
    @Operation(
            summary = "Retrieving all the users by the following filters: mail, role",
            parameters = {
                    @Parameter(
                            name = "fileName",
                            description = "Filtrar por Nombre",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    ),
                    @Parameter(
                            name = "status",
                            description = "Filtrar por Status",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = FileStatus.class)
                    ),
                    @Parameter(
                            name = "fileCategory",
                            description = "Filtrar por Categoria",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = FileCategory.class)
                    ),
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
    @SecurityRequirement(name = "bearerAuth")
    public WebResponsePagination getAllUsers(
            @Parameter(hidden = true)
            @RequestHeader ("Authorization") String bearerToken,
            @RequestParam int page, @RequestParam int size,
            @Parameter(hidden = true)
            @RequestParam Map<String, String> filter)
    {
        filter.remove("page");
        filter.remove("size");
        return userFileService.getFiles(bearerToken,page, size, filter);
    }

    @GetMapping(value = "/get/filesBy/{userId}")
    @Operation(summary = "Encontrar los documentos de un Usuario (endpoint sin filtros)")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documentos extraidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WebResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error en el proceso de firma (Python o Disco)",
                            content = @Content
                    ),
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    public WebResponse getFilesByUserId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String bearerToken,
            @Parameter(description = "Id of user", required = true)
            @Valid @PathVariable("userId") UUID userId
    ) {
        return userFileService.getFilesByUserId(bearerToken,userId);
    }


}
