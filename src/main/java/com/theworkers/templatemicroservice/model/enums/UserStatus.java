package com.theworkers.templatemicroservice.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User enum Status")
public enum UserStatus {
    ACTIVO,
    INACTIVO,
    BAJA_TEMPORAL
}
