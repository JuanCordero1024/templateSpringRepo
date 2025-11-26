package com.theworkers.templatemicroservice.service;

import java.util.UUID;

public interface JWTService {
    String generateToken(UUID userId);
}
