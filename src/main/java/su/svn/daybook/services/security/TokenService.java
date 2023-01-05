/*
 * This file was last modified at 2023.01.05 15:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TokenService.java
 * $Id$
 */

package su.svn.daybook.services.security;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TokenService {

    @ConfigProperty(name = "quarkus.smallrye.jwt.new-token.lifespan")
    Long duration;

    @ConfigProperty(name = "quarkus.smallrye.jwt.new-token.issuer")
    String issuer;

    public String generate(String username, Set<String> roles) {

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        long currentTimeInSecs = currentTimeInSecs();

        Set<String> groups = new HashSet<>(roles);

        claimsBuilder.issuer(issuer);
        claimsBuilder.subject(username);
        claimsBuilder.issuedAt(currentTimeInSecs);
        claimsBuilder.expiresAt(currentTimeInSecs + duration);
        claimsBuilder.groups(groups);

        return claimsBuilder.jws().sign();
    }

    public static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

}
