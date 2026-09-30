package com.bwesound.identity.auth.api;

import java.time.Instant;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt
) {
}
