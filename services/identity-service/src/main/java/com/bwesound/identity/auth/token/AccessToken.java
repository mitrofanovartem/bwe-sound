package com.bwesound.identity.auth.token;

import java.time.Instant;

public record AccessToken(
        String value,
        Instant expiresAt)
{}
