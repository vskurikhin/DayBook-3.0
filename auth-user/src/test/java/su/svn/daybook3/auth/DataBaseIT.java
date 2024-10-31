/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataBaseIT.java
 * $Id$
 */

package su.svn.daybook3.auth;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.auth.domain.dao.RoleDao;
import su.svn.daybook3.auth.domain.dao.SessionDao;
import su.svn.daybook3.auth.domain.dao.UserNameDao;
import su.svn.daybook3.auth.domain.dao.UserViewDao;
import su.svn.daybook3.auth.domain.model.RoleTable;
import su.svn.daybook3.auth.domain.model.SessionTable;
import su.svn.daybook3.auth.domain.model.UserNameTable;
import su.svn.daybook3.auth.domain.model.UserView;
import su.svn.daybook3.auth.domain.transact.UserTransactionalJob;
import su.svn.daybook3.auth.models.domain.User;
import su.svn.daybook3.auth.resources.PostgresDatabaseTestResource;
import su.svn.daybook3.auth.services.models.UserService;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.domain.messages.ApiResponse;
import su.svn.daybook3.domain.messages.Request;
import su.svn.daybook3.models.TimeUpdated;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static su.svn.daybook3.auth.TestUtils.multiAsListHelper;
import static su.svn.daybook3.auth.TestUtils.uniOptionalHelper;
import static su.svn.daybook3.auth.TestUtils.uniToAnswerHelper;

@SuppressWarnings({"SameParameterValue"})
@QuarkusTest
@WithTestResource(value = PostgresDatabaseTestResource.class)
// @QuarkusTestResource(value = PostgresDatabaseTestResource.class, restrictToAnnotatedClass = true)
public class DataBaseIT {
    @Inject
    RoleDao roleDao;
    @Inject
    SessionDao sessionDao;
    @Inject
    UserNameDao userNameDao;
    @Inject
    UserService userService;
    @Inject
    UserTransactionalJob userTransactionalJob;
    @Inject
    UserViewDao userViewDao;

    @Nested
    @DisplayName("RoleDao")
    class RoleDaoTest {
        UUID id = new UUID(0, 1);
        UUID customId = UUID.randomUUID();
        RoleTable entry;

        @BeforeEach
        void setUp() {
            entry = RoleTable.builder()
                    .id(id)
                    .role("role")
                    .userName("root")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.insert(entry))));
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(roleDao.count())));
        }

        RoleTable expected(UUID id, String role, RoleTable test) {
            Assertions.assertNotNull(test);
            return RoleTable.builder()
                    .id(id)
                    .role(role)
                    .userName("root")
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id));
                var expected = expected(id, "role", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });
            var update = RoleTable.builder().id(id).role("none").build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(roleDao.update(update))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id));
                var expected = expected(id, "none", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            var custom = RoleTable.builder()
                    .id(customId)
                    .role("null")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(roleDao.insert(custom))));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
                var expected = expected(customId, "null", test.get(0));
                Assertions.assertEquals(expected, test.get(0));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(2, test.size());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(1, 1));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(customId, uniOptionalHelper(roleDao.delete(customId))));
        }
    }

    @Nested
    @DisplayName("SessionDao")
    class SessionDaoTest {
        UUID id = TestData.uuid.ZERO;
        UUID customId = TestData.uuid.ONE;
        UUID userId = TestData.uuid.ZERO;
        SessionTable entry;

        @BeforeEach
        void setUp() {
            var userName = UserNameTable.builder()
                    .id(userId)
                    .userName(SessionTable.NONE)
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userId, uniOptionalHelper(userNameDao.insert(userName))));
            entry = SessionTable.builder()
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.EPOCH_TIME)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(sessionDao.insert(entry));
            });
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(sessionDao.count())));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userId, uniOptionalHelper(userNameDao.delete(userId))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count())));
        }

        SessionTable expected(UUID id, LocalDateTime validTime, SessionTable test) {
            Assertions.assertNotNull(test);
            return SessionTable.builder()
                    .id(id)
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(validTime)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }


        SessionTable expected(UUID id, String userName, SessionTable test) {
            Assertions.assertNotNull(test);
            return SessionTable.builder()
                    .id(id)
                    .userName(userName)
                    .validTime(test.validTime())
                    .roles(Collections.emptySet())
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }


        void whenSupplierThenList(Supplier<Multi<SessionTable>> supplier, Function<List<SessionTable>, List<SessionTable>> toExpected) {
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(supplier.get());
                Assertions.assertEquals(toExpected.apply(test), test);
                if (test instanceof TimeUpdated timeUpdated) {
                    Assertions.assertNotNull(timeUpdated.createTime());
                    Assertions.assertNull(timeUpdated.updateTime());
                }
            });
        }

        List<SessionTable> expectedSingletonList(UUID id, List<SessionTable> test) {
            Assertions.assertNotNull(test);
            var entry1 = test
                    .stream()
                    .findFirst()
                    .orElse(null);
            var entry2 = expected(id, SessionTable.NONE, entry1);
            return Collections.singletonList(entry2);
        }

        @Test
        void test() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                var expected = expected(id, TestData.time.EPOCH_TIME, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });

            var update = SessionTable.builder()
                    .id(id)
                    .userName(SessionTable.NONE)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.NOW)
                    .build();

            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.update(update)))
            );

            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                var expected = expected(id, TestData.time.NOW, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            whenSupplierThenList(
                    () -> sessionDao.findByValue(SessionTable.NONE),
                    test -> expectedSingletonList(id, test));

            whenSupplierThenList(
                    () -> sessionDao.findByName(SessionTable.NONE),
                    test -> expectedSingletonList(id, test));

            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(sessionDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(1, test.size());
            });


            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(sessionDao.findRange(0, 0));
                Assertions.assertNotNull(test);
                Assertions.assertTrue(test.isEmpty());
            });

        }
    }

    @Nested
    @DisplayName("UserNameDao")
    class UserNameDaoTest extends AbstractDaoTest<UUID, UserNameTable> {

        UUID id = new UUID(0, 1);
        UUID customId = UUID.randomUUID();
        UserNameTable entry;

        @BeforeEach
        void setUp() {
            entry = UserNameTable.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
            super.setUp(userNameDao, entry, customId);
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.delete(id))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count())));
        }

        UserNameTable expected(UUID id, String userName, UserNameTable test) {
            Assertions.assertNotNull(test);
            return UserNameTable.builder()
                    .id(id)
                    .userName(userName)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }

        @Test
        void test() {
            super.whenFindByIdThenEntry((id, test) -> expected(id, "user", test));

            var update = UserNameTable
                    .builder()
                    .id(super.id)
                    .userName("userName1")
                    .build();
            super.whenUpdateAndFindByIdThenEntry((id, test) -> expected(id, "userName1", test), update);

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();

            var custom = UserNameTable
                    .builder()
                    .id(customId)
                    .userName("user2")
                    .build();
            super.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, "user2", test),
                    custom
            );
            var customUpdate = UserNameTable
                    .builder()
                    .id(customId)
                    .userName("userName2")
                    .build();
            super.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, "userName2", test),
                    customUpdate
            );

            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
            super.whenFindRangeFromOneLimitOneMultiWithOneItem();

            super.whenDeleteCustomThenOk();
        }
    }

    @Nested
    @DisplayName("UserService")
    class UserServiceTest {

        UUID id = new UUID(0, 0);
        UUID id1 = new UUID(0, 1);
        UUID id2 = new UUID(0, 2);
        UUID customId = UUID.randomUUID();
        RoleTable role1;
        RoleTable role2;
        User user;
        Principal principal;

        @BeforeEach
        void setUp() {
            role1 = RoleTable.builder().id(id1).role("role1").userName("root").build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.insert(role1))));
            role2 = RoleTable.builder().id(id2).role("role2").userName("root").build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.insert(role2))));
            user = User.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .roles(Collections.emptySet())
                    .build();
            principal = null/*new QuarkusPrincipal(null)*/;
        }

        @AfterEach
        void tearDown() {
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.delete(id2))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.delete(id1))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(roleDao.count())));
            checkUserNameTableIsEmpty();
        }

        @Test
        void test() {
            var expected1 = userServiceAddUser();
            deleteUserName();
            checkUserNameTableIsEmpty();
            for (var roles : new String[][]{{"role1"}, {"role1", "role2"}}) {
                userServiceAddUserWithRoles(expected1, roles);
                checkUserNameTableIsEmpty();
            }
            var expected2 = Answer.builder()
                    .message(Answer.DEFAULT_MESSAGE)
                    .error(202)
                    .payload(new ApiResponse<>(TestData.uuid.ZERO, 202))
                    .build();
            for (var roles : new String[][]{{"role1"}, {"role1", "role2"}}) {
                userServicePutUserWithRoles(expected1, expected2, roles);
                checkUserNameTableIsEmpty();
            }
            var custom = User.builder()
                    .userName("userName")
                    .password("password")
                    .roles(Set.of("role1", "role2"))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                Answer actual = uniToAnswerHelper(userService.add(new Request<>(custom, principal)));
                if (actual.payload() instanceof ApiResponse apiResponse) {
                    customId = UUID.fromString(apiResponse.id().toString());
                    Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                            customId, uniOptionalHelper(userNameDao.delete(customId))
                    ));
                }
            });
        }

        @NotNull
        private Answer userServiceAddUser() {
            var expected = Answer.builder()
                    .message(Answer.DEFAULT_MESSAGE)
                    .error(201)
                    .payload(new ApiResponse<>(TestData.uuid.ZERO, 201))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
            });
            return expected;
        }

        private void userServiceAddUserWithRoles(Answer expected, String[] roles) {
            var set = Set.of(roles);
            user = User.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .roles(set)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(set.size(), userView.roles().size());
            });
            userServiceDeleteByUserId();
        }

        private void userServicePutUserWithRoles(Answer expected, Answer expected2, String[] roles) {
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
            });
            var set = Set.of(roles);
            var first = set.stream().findFirst();
            Assertions.assertFalse(first.isEmpty());
            user = User.builder()
                    .id(id)
                    .userName("user")
                    .password(first.get())
                    .roles(set)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.put(new Request<>(user, principal)));
                Assertions.assertEquals(expected2, actual);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(set.size(), userView.roles().size());
            });
            userServiceDeleteByUserId();
        }

        private void userServiceDeleteByUserId() {
            Assertions.assertDoesNotThrow(() -> {
                var expected200 = Answer.builder()
                        .message(Answer.DEFAULT_MESSAGE)
                        .error(200)
                        .payload(new ApiResponse<>(TestData.uuid.ZERO, 200))
                        .build();
                var actual = uniToAnswerHelper(userService.delete(new Request<>(user.id(), principal)));
                Assertions.assertNotNull(actual);
                Assertions.assertEquals(expected200, actual);
                if (actual.payload() instanceof ApiResponse apiResponse) {
                    Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                            id, UUID.fromString(apiResponse.id().toString())
                    ));
                }
            });
        }

        private void checkUserNameTableIsEmpty() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count()))
            );
        }

        private void deleteUserName() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                    id, uniOptionalHelper(userNameDao.delete(id))
            ));
        }
    }

    @Nested
    @DisplayName("UserViewDao")
    class UserViewDaoTest extends AbstractViewDaoTest<UUID, UserNameTable, UserView> {

        @BeforeEach
        void setUp() {
            var entry1 = UserNameTable.builder()
                    .id(UUID.randomUUID())
                    .userName("userName1")
                    .enabled(true)
                    .build();
            var entry2 = UserNameTable.builder()
                    .id(UUID.randomUUID())
                    .userName("userName2")
                    .enabled(true)
                    .build();
            super.setUp(userNameDao, userViewDao, entry1, entry2);
        }

        @AfterEach
        void tearDown() {
            super.tearDown();
        }

        UserView.Builder builder(UUID id, String userName, UserView test) {
            return UserView.builder()
                    .id(id)
                    .userName(userName)
                    .password("password")
                    .roles(Collections.emptySet())
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true);
        }

        UserView expected(UUID id, String userName, UserView test) {
            Assertions.assertNotNull(test);
            return builder(id, userName, test).build();
        }

        @Test
        void test() {
            super.checkCount2();

            super.whenFindById1ThenEntry((id, test) -> expected(id, "userName1", test));
            super.whenFindById2ThenEntry((id, test) -> expected(id, "userName2", test));

            super.whenSupplierThenEntry(
                    () -> userViewDao.findByKey("userName1"),
                    test -> expected(super.id1, "userName1", test));

            super.whenSupplierThenEntry(
                    () -> userViewDao.findByUserName("userName1"),
                    test -> expected(super.id1, "userName1", test));

            super.whenFindAllThenMultiWithOneItem();
            super.whenFindRangeZeroThenEmptiestMulti();
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems();
        }
    }

    @Nested
    @DisplayName("UserTransactionalJob")
    class UserTransactionalJobTest {
        UUID id = new UUID(0, 0);
        UUID id1 = new UUID(0, 1);
        UUID id2 = new UUID(0, 2);
        UUID customId = UUID.randomUUID();
        RoleTable role1;
        RoleTable role2;
        UserNameTable userName;

        @BeforeEach
        void setUp() {
            role1 = RoleTable.builder()
                    .id(id1)
                    .role("role1")
                    .userName("root")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.insert(role1))));
            role2 = RoleTable.builder()
                    .id(id2)
                    .role("role2")
                    .userName("root")
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.insert(role2))));
            userName = UserNameTable.builder()
                    .id(id)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
        }

        @AfterEach
        void tearDown() {
            checkUserNameTableIsEmpty();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.delete(id2))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.delete(id1))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(roleDao.count())));
        }

        @Test
        void test() {
            userNameWithoutRolesInserted();
            deleteUserName();
            checkUserNameTableIsEmpty();
            userNameWithRoleROLE1Inserted();
            deleteUserName();
            checkUserNameTableIsEmpty();
            userNameWithRolesROLE1AndROLE2Inserted();
            userNameWithRoleROLE1Updated();
            deleteUserName();
            checkUserNameTableIsEmpty();
            userNameWithRolesROLE1AndROLE2AndROLE3TryInsertingGotExeption();
            checkUserNameTableIsEmpty();
            var custom = UserNameTable.builder()
                    .userName("userName")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                customId = uniOptionalHelper(userTransactionalJob.insert(custom, Set.of("role1", "role2")));
            });
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(customId, uniOptionalHelper(userNameDao.delete(customId))));
        }

        private void userNameWithoutRolesInserted() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.emptySet()));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(0, userView.roles().size());
            });

        }

        private void userNameWithRoleROLE1Inserted() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.singleton("role1")));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(1, userView.roles().size());
            });
        }

        private void userNameWithRolesROLE1AndROLE2Inserted() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2")));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(2, userView.roles().size());
            });
        }

        private void userNameWithRoleROLE1Updated() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.update(userName, Set.of("role1")));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id, test);
                var userView = uniOptionalHelper(userViewDao.findById(id));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(1, userView.roles().size());
            });
        }

        private void userNameWithRolesROLE1AndROLE2AndROLE3TryInsertingGotExeption() {
            Assertions.assertThrows(java.util.concurrent.ExecutionException.class,
                    () -> uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2", "role3"))));
        }

        private void checkUserNameTableIsEmpty() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(0, uniOptionalHelper(userNameDao.count())));
        }

        private void deleteUserName() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id, uniOptionalHelper(userNameDao.delete(id))));
        }
    }
}
