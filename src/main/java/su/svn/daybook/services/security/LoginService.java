/*
 * This file was last modified at 2023.01.05 15:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginService.java
 * $Id$
 */

package su.svn.daybook.services.security;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.models.security.AuthRequest;
import su.svn.daybook.models.security.User;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

@ApplicationScoped
public class LoginService {

    @Inject
    PBKDF2Encoder passwordEncoder;

    public Uni<Set<String>> login(@Nonnull AuthRequest authRequest) {
        return Uni
                .createFrom()
                .item(User.findByUsername(authRequest.username()))
                .map(user1 -> login(authRequest, user1));
    }

    private Set<String> login(AuthRequest authRequest, User user) {

        if (user.password() != null) {
            if (user.password().equals(passwordEncoder.encode(authRequest.password()))) {
                return user.roles();
            }
        }
        throw new AuthenticationFailedException("Authentication failed");
    }
}
