/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TokenService.java
 * $Id$
 */

package su.svn.daybook3.auth.services.security;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ApplicationScoped
public class TokenService {

    @ConfigProperty(name = "quarkus.smallrye.jwt.new-token.lifespan")
    Long duration;

    @ConfigProperty(name = "quarkus.smallrye.jwt.new-token.issuer")
    String issuer;

    public String generate(String username, String audience) {

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        long currentTimeInSecs = currentTimeInSecs();

        claimsBuilder.issuer(issuer);
        claimsBuilder.subject(username);
        claimsBuilder.issuedAt(currentTimeInSecs);
        claimsBuilder.expiresAt(currentTimeInSecs + duration);
        claimsBuilder.audience(audience);

        return claimsBuilder.jws().sign();
    }

    public LocalDateTime validTime() {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis() + 1000 * duration),
                ZoneId.systemDefault()
        );
    }

    public static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

}
