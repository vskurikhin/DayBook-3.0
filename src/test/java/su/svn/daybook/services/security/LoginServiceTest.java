package su.svn.daybook.services.security;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import su.svn.daybook.TestData;
import su.svn.daybook.domain.dao.SessionDao;
import su.svn.daybook.domain.dao.UserViewDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.SessionTable;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.models.security.AuthRequest;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static su.svn.daybook.TestUtils.uniToAnswerHelper;

@QuarkusTest
class LoginServiceTest {

    private static final String PASSWORD = "password";

    static SessionDao mockSessionDao;

    static UserViewDao mockUserViewDao;

    static AuthRequest stubAuthRequest = new AuthRequest(SessionTable.NONE, PASSWORD);

    static Uni<Optional<UserView>> UNI_OPTIONAL_EMPTY = Uni.createFrom().item(Optional.empty());

    Uni<Optional<UserView>> stubUOUserView;

    @Inject
    PBKDF2Encoder passwordEncoder;

    @Inject
    LoginService loginService;

    @BeforeEach
    void setUp() {
        mockSessionDao = Mockito.mock(SessionDao.class);
        mockUserViewDao = Mockito.mock(UserViewDao.class);
        Mockito.when(mockSessionDao.insert(ArgumentMatchers.any(SessionTable.class)))
                .thenReturn(TestData.uuid.UNI_OPTIONAL_ZERO);
        QuarkusMock.installMockForType(mockSessionDao, SessionDao.class);
        QuarkusMock.installMockForType(mockUserViewDao, UserViewDao.class);
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
            var answer = uniToAnswerHelper(loginService.login(stubAuthRequest));
            Assertions.assertEquals("ANSWER", answer.getMessage());
            Assertions.assertEquals(200, answer.getError());
            Assertions.assertEquals(ApiResponse.class, answer.getPayloadClass());
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
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(expected, uniToAnswerHelper(loginService.login(stubAuthRequest)));
        });
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
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(expected, uniToAnswerHelper(loginService.login(stubAuthRequest)));
        });
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
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(expected, uniToAnswerHelper(loginService.login(stubAuthRequest)));
        });
    }
}