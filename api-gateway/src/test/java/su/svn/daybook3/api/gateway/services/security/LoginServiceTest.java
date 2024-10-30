/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginServiceTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.security;

import io.quarkus.cache.CacheManager;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.domain.dao.SessionDao;
import su.svn.daybook3.api.gateway.domain.dao.UserViewDao;
import su.svn.daybook3.api.gateway.domain.enums.EventAddress;
import su.svn.daybook3.api.gateway.domain.model.SessionTable;
import su.svn.daybook3.api.gateway.domain.model.UserView;
import su.svn.daybook3.api.gateway.models.security.AuthRequest;
import su.svn.daybook3.api.gateway.utils.CacheUtil;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static su.svn.daybook3.api.gateway.TestUtils.uniToAnswerHelper;

@QuarkusTest
class LoginServiceTest {

    private static final String PASSWORD = "password";

    static SessionDao mockSessionDao;

    static UserViewDao mockUserViewDao;

    static AuthRequest stubAuthRequest = new AuthRequest(SessionTable.NONE, PASSWORD);

    static Uni<Optional<UserView>> UNI_OPTIONAL_EMPTY = Uni.createFrom().item(Optional.empty());

    Uni<Optional<UserView>> stubUOUserView;

    @Inject
    CacheManager cacheManager;

    @Inject
    PBKDF2Encoder passwordEncoder;

    @Inject
    LoginService loginService;

    Principal principal;

    @BeforeEach
    void setUp() {
        principal = new QuarkusPrincipal(null);
        mockSessionDao = Mockito.mock(SessionDao.class);
        mockUserViewDao = Mockito.mock(UserViewDao.class);
        Mockito.when(mockSessionDao.insert(ArgumentMatchers.any(SessionTable.class)))
                .thenReturn(TestData.uuid.UNI_OPTIONAL_ZERO);
        QuarkusMock.installMockForType(mockSessionDao, SessionDao.class);
        QuarkusMock.installMockForType(mockUserViewDao, UserViewDao.class);
        CacheUtil.invalidateAll(cacheManager, EventAddress.LOGIN_REQUEST);
    }

    @Test
    void loginSuccess() {
        var stubUserView = UserView.builder()
                .id(TestData.uuid.ZERO)
                .userName(SessionTable.NONE)
                .password(passwordEncoder.encode(PASSWORD))
                .roles(Collections.emptySet())
                .build();
        stubUOUserView = Uni.createFrom().item(Optional.of(stubUserView));
        Mockito.when(mockUserViewDao.findByUserName(SessionTable.NONE)).thenReturn(stubUOUserView);
        Assertions.assertDoesNotThrow(() -> {
            var answer = uniToAnswerHelper(loginService.login(new Request<>(stubAuthRequest, principal)));
            Assertions.assertEquals("ANSWER", answer.message());
            Assertions.assertEquals(200, answer.error());
            Assertions.assertEquals(ApiResponse.class, answer.payloadClass());
        });
    }

    @Test
    void loginFailedIncorrectPassword() {
        var stubUserView = UserView.builder()
                .id(TestData.uuid.ZERO)
                .userName(SessionTable.NONE)
                .roles(Collections.emptySet())
                .build();
        stubUOUserView = Uni.createFrom().item(Optional.of(stubUserView));
        Mockito.when(mockUserViewDao.findByUserName(SessionTable.NONE)).thenReturn(stubUOUserView);
        var expected = Answer.builder()
                .message("unauthorized")
                .error(401)
                .payload("Authentication failed: " + SessionTable.NONE + " password incorrect!")
                .build();
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertEquals(
                        expected, uniToAnswerHelper(loginService.login(new Request<>(stubAuthRequest, principal)))
                ));
    }

    @Test
    void loginFailedIncorrectUserName() {
        var stubUserView = UserView.builder()
                .id(TestData.uuid.ZERO)
                .userName(SessionTable.NONE)
                .roles(Collections.emptySet())
                .build();
        stubUOUserView = Uni.createFrom().item(Optional.of(stubUserView));
        Mockito.when(mockUserViewDao.findByUserName(SessionTable.NONE)).thenReturn(UNI_OPTIONAL_EMPTY);
        var expected = Answer.builder()
                .message("unauthorized")
                .error(401)
                .payload("Authentication failed: " + SessionTable.NONE + " user name incorrect!")
                .build();
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertEquals(
                        expected, uniToAnswerHelper(loginService.login(new Request<>(stubAuthRequest, principal)))
                ));
    }

    @Test
    void loginFailedCreateSessionFail() {
        var stubUserView = UserView.builder()
                .id(TestData.uuid.ZERO)
                .userName(SessionTable.NONE)
                .password(passwordEncoder.encode(PASSWORD))
                .roles(Collections.emptySet())
                .build();
        stubUOUserView = Uni.createFrom().item(Optional.of(stubUserView));
        Mockito.when(mockUserViewDao.findByUserName(SessionTable.NONE)).thenReturn(stubUOUserView);
        Mockito.when(mockSessionDao.insert(ArgumentMatchers.any(SessionTable.class)))
                .thenReturn(TestData.uuid.UNI_OPTIONAL_EMPTY);
        var expected = Answer.builder()
                .message("unauthorized")
                .error(401)
                .payload("Create session fail!")
                .build();
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertEquals(
                        expected, uniToAnswerHelper(loginService.login(new Request<>(stubAuthRequest, principal)))
                ));
    }
}