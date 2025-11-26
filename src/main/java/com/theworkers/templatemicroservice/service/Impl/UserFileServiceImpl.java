package com.theworkers.templatemicroservice.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theworkers.templatemicroservice.model.UserFile;
import com.theworkers.templatemicroservice.model.enums.FileStatus;
import com.theworkers.templatemicroservice.model.output.WebResponsePagination;
import com.theworkers.templatemicroservice.util.CustomMultipartFile;
import com.theworkers.templatemicroservice.model.input.UserFileInputDTO;
import com.theworkers.templatemicroservice.model.input.UserFileSignInputDTO;
import com.theworkers.templatemicroservice.model.mappers.UserFileMapper;
import com.theworkers.templatemicroservice.model.output.UserFileOutputDTO;
import com.theworkers.templatemicroservice.model.output.UserOutputDTO;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import com.theworkers.templatemicroservice.repository.UserFileRepository;
import com.theworkers.templatemicroservice.service.SignServiceClient;
import com.theworkers.templatemicroservice.service.UserFileService;
import com.theworkers.templatemicroservice.service.UserServiceClient;
import com.theworkers.templatemicroservice.util.FilesHelper;
import com.theworkers.templatemicroservice.util.ValidateWebException;
import com.theworkers.templatemicroservice.util.specificators.UserFileSpecificationBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

@Service
public class UserFileServiceImpl implements UserFileService {

    private final UserFileRepository userFileRepository;
    private final UserFileMapper userFileMapper;
    private final UserServiceClient userServiceClient;
    private final SignServiceClient signServiceClient;
    private final ObjectMapper objectMapper;
    private final FilesHelper filesHelper;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public UserFileServiceImpl(UserFileRepository userFileRepository,
                               UserFileMapper userFileMapper,
                               ObjectMapper objectMapper,
                               UserServiceClient userServiceClient,
                               SignServiceClient signServiceClient,
                               FilesHelper filesHelper) {
        this.userFileRepository = userFileRepository;
        this.userFileMapper = userFileMapper;
        this.userServiceClient = userServiceClient;
        this.signServiceClient = signServiceClient;
        this.objectMapper = objectMapper;
        this.filesHelper = filesHelper;
    }

    @Transactional
    public WebResponse uploadFile(String jwtToken,UserFileInputDTO newUserFileInputDTO, MultipartFile file) {
        try{
            WebResponse verifyUser = userServiceClient.readUser(jwtToken, newUserFileInputDTO.getUserId());
            UserOutputDTO userDetails = objectMapper.convertValue(verifyUser.getEntity(), UserOutputDTO.class);
            newUserFileInputDTO.setFileType("pdf");
            newUserFileInputDTO.setStatus(FileStatus.PENDIENTE);
            String fileExtension = filesHelper.getFileExtension(file.getOriginalFilename());
            String storageFileName = UUID.randomUUID().toString() + fileExtension;

            Path destinationPath = Paths.get(uploadDir).resolve(storageFileName).toAbsolutePath();

            // Asegurarnos que la carpeta existe
            Files.createDirectories(destinationPath.getParent());

            // 3. Obtener Bytes y Calcular Hash (Integridad)
            byte[] fileBytes = file.getBytes();
            String fileHash = filesHelper.calculateSha256(fileBytes);
            // 4. Guardar archivo físico en disco
            Files.write(destinationPath, fileBytes);

            // 5. Guardar metadatos en Base de Datos
            UserFile userFile = userFileMapper.toEntity(newUserFileInputDTO);

            // Rutas y Hashes
            userFile.setOriginalFilePath(destinationPath.toString());
            userFile.setOriginalFileHash(fileHash);

            // Guardamos
            UserFile savedFile = userFileRepository.save(userFile);
            UserFileOutputDTO responseEntity = userFileMapper.toOutput(savedFile);
            return WebResponse.builder()
                    .codeStatus(HttpStatus.ACCEPTED)
                    .message("Archivo subido exitosamente")
                    .entity(responseEntity).build();
        } catch (ValidateWebException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo en disco: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error interno al procesar el archivo: " + e.getMessage());
        }
    }

    public ResponseEntity<Resource> downloadFile(String jwtToken,UUID userFileId, FileStatus fileStatus) {
        try {
            // 1. Buscar metadatos en la BD
            UserFile userFile = userFileRepository.findById(userFileId)
                    .orElseThrow(() -> new ValidateWebException("Archivo no encontrado en BD", HttpStatus.BAD_REQUEST));

            String pathOnDisk;
            if (fileStatus.equals(userFile.getStatus()) && userFile.getSignedFilePath() != null) {
                pathOnDisk = userFile.getSignedFilePath();
            } else {
                pathOnDisk = userFile.getOriginalFilePath();
            }

            Path filePath = Paths.get(pathOnDisk);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {

                // 3. Detectar el tipo de archivo automáticamente (PDF vs Word)
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                var responseBuilder = ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + userFile.getFileName() + "\"");

                return responseBuilder.body(resource);

            } else {
                throw new RuntimeException("No se puede leer el archivo físico en ruta: " + pathOnDisk);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al descargar: " + e.getMessage());
        }
    }

    public ResponseEntity<Resource> previewFile(String jwtToken,UUID userFileId, FileStatus fileStatus) {
        try {
            // 1. Buscar metadatos en la BD
            UserFile userFile = userFileRepository.findById(userFileId)
                    .orElseThrow(() -> new ValidateWebException("Archivo no encontrado en BD", HttpStatus.BAD_REQUEST));

            String pathOnDisk;
            if (fileStatus.equals(userFile.getStatus()) && userFile.getSignedFilePath() != null) {
                pathOnDisk = userFile.getSignedFilePath();
            } else {
                pathOnDisk = userFile.getOriginalFilePath();
            }

            Path filePath = Paths.get(pathOnDisk);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {

                // 3. Detectar el tipo de archivo automáticamente (PDF vs Word)
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                var responseBuilder = ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + userFile.getFileName() + "\"");

                return responseBuilder.body(resource);

            } else {
                throw new RuntimeException("No se puede leer el archivo físico en ruta: " + pathOnDisk);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al descargar: " + e.getMessage());
        }
    }

    public WebResponse signFile(String jwtToken, UserFileSignInputDTO newUserFileSignInputDTO) {
        try {
            // 1. Buscar registro y datos de usuario
            UserFile doc = userFileRepository.getUserFileById(newUserFileSignInputDTO.getDocumentId());
            WebResponse verifyUser = userServiceClient.readUser(jwtToken, newUserFileSignInputDTO.getUserId());
            UserOutputDTO user = objectMapper.convertValue(verifyUser.getEntity(), UserOutputDTO.class);

            // 2. Validar archivo físico
            File fileOriginal = new File(doc.getOriginalFilePath());
            if (!fileOriginal.exists()) {
                throw new FileNotFoundException("El archivo físico no existe en la ruta: " + doc.getOriginalFilePath());
            }

            // 3. Convertir File -> CustomMultipartFile (SOLUCIÓN DEL ERROR)
            MultipartFile multipartFile = new CustomMultipartFile(fileOriginal, "archivo");
            MultipartFile multipartPrivateKey = new CustomMultipartFile(
                    user.getPrivateKey().getBytes(StandardCharsets.UTF_8),
                    "private_key",
                    "private_key.txt",
                    "text/plain"
            );

            // Llamado a microservicio de Python
            feign.Response response = signServiceClient.signDocument(
                    user.getName() + " " + user.getLastName(),
                    doc.getFileType(),
                    multipartPrivateKey,
                    multipartFile
            );

            // 5. Procesar respuesta exitosa
            if (response.status() == 200) {
                if (!response.headers().containsKey("X-Digital-Signature")) {
                    throw new RuntimeException("El servicio de firma no devolvió el header X-Digital-Signature");
                }
                String digitalSignature = response.headers().get("X-Digital-Signature").iterator().next();

                // B. Definir ruta de destino (Usando la misma carpeta del original)
                Path originalPath = fileOriginal.toPath();
                Path parentDir = originalPath.getParent();
                String signedFileName = "signed_" + fileOriginal.getName();
                Path targetPath = parentDir.resolve(signedFileName);

                // C. Guardar archivo firmado
                // Usamos try-with-resources para cerrar el stream automáticamente
                try (InputStream signedInputStream = response.body().asInputStream()) {
                    Files.copy(signedInputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }

                // D. Actualizar BD
                doc.setSignedFilePath(targetPath.toString());
                doc.setDigitalSignature(digitalSignature);
                doc.setStatus(FileStatus.FIRMADO);
                userFileRepository.save(doc);

                return new WebResponse("Documento firmado exitosamente", HttpStatus.OK, doc);
            } else {
                String errorBody = "";
                try {
                    if (response.body() != null) {
                        errorBody = new String(response.body().asInputStream().readAllBytes());
                    }
                } catch (IOException e) {
                    errorBody = "No se pudo leer el cuerpo del error.";
                }

                System.err.println("❌ ERROR DE PYTHON (BODY): " + errorBody);
                throw new RuntimeException("Error en microservicio de firma. Status: " + response.status() + " Detalle: " + errorBody);
            }

        } catch (ValidateWebException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error interno al firmar: " + e.getMessage(), e);
        }
    }

    public WebResponse getFilesByUserId(String jwtToken,UUID userId) {
        try{
            WebResponse verifyUser = userServiceClient.readUser(jwtToken, userId);
            if(verifyUser.getCodeStatus().equals(HttpStatus.UNAUTHORIZED) || verifyUser.getCodeStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                return verifyUser;
            UserOutputDTO user = objectMapper.convertValue(verifyUser.getEntity(), UserOutputDTO.class);
            List<UserFileOutputDTO> response = userFileMapper.toOutputList(userFileRepository.getUserFilesByUserId(user.getId()));
            return new WebResponse("Returning data of: " + user.getName(), HttpStatus.OK, response);
        }catch (ValidateWebException e){throw e;}
    }

    public WebResponsePagination getFiles(String jwtToken, int page, int size, Map<String, String> filters) {
        Specification<UserFile> spec = new UserFileSpecificationBuilder(filters).build();

        // 2. Paginación
        Pageable pageable = PageRequest.of(page - 1, size);

        // 3. Buscar
        Page<UserFile> resultPage = userFileRepository.findAll(spec, pageable);
        List<UserFileOutputDTO> response = resultPage.getContent()
                .stream()
                .map(userFileMapper::toOutput)
                .collect(Collectors.toList());

        return new WebResponsePagination(
                resultPage.isEmpty()? "No data": "Retrieving data",
                HttpStatus.OK,
                response,
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                resultPage.getNumber()+1
        );

    }

}