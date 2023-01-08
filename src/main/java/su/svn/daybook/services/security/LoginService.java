/*
 * This file was last modified at 2023.01.05 15:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginService.java
 * $Id$
 */

package su.svn.daybook.services.security;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import su.svn.daybook.domain.dao.SessionDao;
import su.svn.daybook.domain.dao.UserViewDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.SessionTable;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.models.security.AuthRequest;
import su.svn.daybook.models.security.User;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class LoginService {

    public static final String AUTHENTICATION_FAILED = "Authentication failed: ";
    public static final String USER_NAME_INCORRECT = " user name incorrect!";
    public static final String PASSWORD_INCORRECT = " password incorrect!";

    @Inject
    PBKDF2Encoder passwordEncoder;

    @Inject
    TokenService tokenService;

    @Inject
    SessionDao sessionDao;

    @Inject
    UserViewDao userViewDao;

    @ConsumeEvent(EventAddress.LOGIN_REQUEST)
    public Uni<Answer> login(@Nonnull AuthRequest authRequest) {
        return userViewDao
                .findByUserName(authRequest.username())
                .flatMap(o -> authentication(authRequest, o));
    }

    private Uni<Answer> authentication(AuthRequest authRequest, Optional<UserView> optionalUser) {
        return optionalUser
                .map(u -> authentication(authRequest, User.from(u)))
                .orElseThrow(() -> authenticationFailed(authRequest, true));
    }

    private Uni<Answer> authentication(AuthRequest authRequest, User user) {

        if (user.password() != null) {
            if (user.password().equals(passwordEncoder.encode(authRequest.password()))) {
                return generateToken(authRequest, user).map(token -> Answer.of(ApiResponse.auth(token)));
            }
        }
        throw authenticationFailed(authRequest,false);
    }

    private AuthenticationFailedException authenticationFailed(AuthRequest authRequest, boolean unknownUser) {
        return unknownUser
                ? new AuthenticationFailedException(AUTHENTICATION_FAILED + authRequest.username() + USER_NAME_INCORRECT)
                : new AuthenticationFailedException(AUTHENTICATION_FAILED + authRequest.username() + PASSWORD_INCORRECT);
    }

    private Uni<String> generateToken(AuthRequest authRequest, User user) {
        SessionTable session = SessionTable.builder()
                .userName(user.username())
                .roles(user.roles())
                .validTime(tokenService.validTime())
                .build();
        return sessionDao
                .insert(session)
                .map(o -> lookUpUUID(authRequest, user, o));
    }

    private String lookUpUUID(AuthRequest authRequest, User user, Optional<UUID> uuidOptional) {
        return uuidOptional
                .map(uuid -> generateToken(user, uuid))
                .orElseThrow(() -> authenticationFailed(authRequest, false));
    }

    private String generateToken(User user, UUID u) {
        return tokenService.generate(u.toString(), user.roles());
    }
}