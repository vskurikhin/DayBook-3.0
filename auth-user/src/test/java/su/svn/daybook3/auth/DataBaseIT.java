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
import org.junit.jupiter.api.*;
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
import java.util.stream.Collectors;

import static su.svn.daybook3.auth.TestUtils.*;

@SuppressWarnings({"SameParameterValue"})
@QuarkusTest
@WithTestResource(value = PostgresDatabaseTestResource.class)
public class DataBaseIT {
    public static final String ROOT = "root";
    public static final String ADMIN = "ADMIN";
    public static final String GUEST = "GUEST";
    public static final String USER = "USER";

    @Inject
    RoleDao roleDao;
    @Inject
    SessionDao sessionDao;
    @Inject
    UserNameDao userNameDao;
    @Inject
    UserService userService;
    @Inject
    UserViewDao userViewDao;
    @Inject
    UserTransactionalJob userTransactionalJob;

    UUID id0 = TestData.uuid.ZERO;
    UUID id1 = TestData.uuid.ONE;
    UUID id2 = new UUID(0, 2);

    @Nested
    @DisplayName("RoleDao")
    class RoleDaoTest {
        @BeforeEach
        void setUp() {
            checkPreSet();
        }

        @AfterEach
        void tearDown() {
            checkPreSet();
        }

        @Test
        void test() {
            var entry = RoleTable.builder()
                    .id(id1)
                    .role("role")
                    .userName(ROOT)
                    .build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.insert(entry))));

            var update = RoleTable.builder().id(id1).role("none").userName(ROOT).build();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.update(update))));
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(roleDao.findById(id1));
                var expected = expected(id1, "none", test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(4, test.size());
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
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findRange(0, Long.MAX_VALUE));
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertEquals(4, test.size());
            });

            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.delete(id1))));
        }

        private void checkPreSet() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(3, uniOptionalHelper(roleDao.count())));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(roleDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertTrue(contains(test, ADMIN));
                Assertions.assertTrue(contains(test, GUEST));
                Assertions.assertTrue(contains(test, USER));
            });
        }

        private static boolean contains(List<RoleTable> test, String role) {
            return test.stream()
                    .map(RoleTable::role)
                    .collect(Collectors.toSet())
                    .contains(role);
        }

        RoleTable expected(UUID id, String role, RoleTable test) {
            Assertions.assertNotNull(test);
            return RoleTable.builder()
                    .id(id)
                    .role(role)
                    .userName(ROOT)
                    .createTime(test.createTime())
                    .updateTime(test.updateTime())
                    .enabled(true)
                    .build();
        }
    }

    @Nested
    @DisplayName("SessionDao")
    class SessionDaoTest {
        UUID id;

        @BeforeEach
        void setUp() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(sessionDao.count())));
        }

        @AfterEach
        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, uniOptionalHelper(sessionDao.count())));
        }

        @Test
        void test() {
            var entry = SessionTable.builder()
                    .userName(ROOT)
                    .roles(Collections.emptySet())
                    .validTime(TestData.time.EPOCH_TIME)
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                id = uniOptionalHelper(sessionDao.insert(entry));
            });
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                var expected = expected(id, TestData.time.EPOCH_TIME, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNull(test.updateTime());
            });
            whenSupplierThenList(
                    () -> sessionDao.findByValue(ROOT),
                    test -> expectedSingletonList(id, test));

            whenSupplierThenList(
                    () -> sessionDao.findByName(ROOT),
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

            var roles = Collections.singleton(ADMIN);
            var update = SessionTable.builder()
                    .id(id)
                    .userName(ROOT)
                    .roles(roles)
                    .validTime(TestData.time.NOW)
                    .build();

            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.update(update)))
            );
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(sessionDao.findById(id));
                var expected = expected(id, TestData.time.NOW, roles, test);
                Assertions.assertEquals(expected, test);
                Assertions.assertNotNull(test.createTime());
                Assertions.assertNotNull(test.updateTime());
            });

            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id, uniOptionalHelper(sessionDao.delete(id))));
        }

        SessionTable expected(UUID id, SessionTable test) {
            Assertions.assertNotNull(test);
            return expected(id, test.validTime(), Collections.emptySet(), test);
        }

        SessionTable expected(UUID id, LocalDateTime validTime, SessionTable test) {
            Assertions.assertNotNull(test);
            return expected(id, validTime, Collections.emptySet(), test);
        }

        SessionTable expected(UUID id, LocalDateTime validTime, Set<String> roles, SessionTable test) {
            Assertions.assertNotNull(test);
            return SessionTable.builder()
                    .id(id)
                    .userName(ROOT)
                    .roles(roles)
                    .validTime(validTime)
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
            var entry2 = expected(id, entry1);
            return Collections.singletonList(entry2);
        }
    }

    static class UserNameDaoHelper extends AbstractDaoTest<UUID, UserNameTable> {
    }

    @Nested
    @DisplayName("UserNameDao")
    class UserNameDaoTest {
        UserNameDaoHelper helper = new UserNameDaoHelper();

        @BeforeEach
        void setUp() {
            checkPreSet();
        }

        @AfterEach
        void tearDown() {
            checkPreSet();
        }

        @Test
        void test() {
            var entry = UserNameTable.builder()
                    .id(id1)
                    .userName("user")
                    .password("password")
                    .enabled(true)
                    .build();
            var idRandom = UUID.randomUUID();
            helper.setUp(userNameDao, entry, idRandom);
            helper.whenFindByIdThenEntry((id1, test) -> expected(id1, "user", test));
            helper.whenFindAllThenMultiWithOneItem();
            helper.whenFindRangeZeroThenEmptiestMulti();
            helper.whenFindRangeFromZeroLimitOneThenMultiWithOneItem();
            helper.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems(2);
            helper.whenFindRangeFromOneLimitOneMultiWithOneItem();
            var custom = UserNameTable
                    .builder()
                    .id(idRandom)
                    .userName("user2")
                    .build();
            helper.whenInsertCustomThenEntry(
                    (id, test) -> expected(id, "user2", test),
                    custom
            );
            var customUpdate = UserNameTable
                    .builder()
                    .id(idRandom)
                    .userName("userName2")
                    .build();
            helper.whenUpdateCustomAndFindByIdThenEntry(
                    (id, test) -> expected(id, "userName2", test),
                    customUpdate
            );
            helper.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems(3);
            helper.whenDeleteCustomThenOk();
            helper.tearDown();
        }

        private void checkPreSet() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count())));
            Assertions.assertDoesNotThrow(() -> {
                var test = multiAsListHelper(userNameDao.findAll());
                Assertions.assertNotNull(test);
                Assertions.assertFalse(test.isEmpty());
                Assertions.assertTrue(contains(test, "root"));
            });
        }

        private static boolean contains(List<UserNameTable> test, String role) {
            return test.stream()
                    .map(UserNameTable::userName)
                    .collect(Collectors.toSet())
                    .contains(role);
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
    }

    static class RoleTableDaoHelper {
        static final String ROLE_1 = "role1";
        static final String ROLE_2 = "role2";

        RoleDao roleDao;
        RoleTable role1;
        RoleTable role2;
        UUID id1;
        UUID id2;

        RoleTableDaoHelper(RoleDao roleDao, UUID id1, UUID id2, RoleTable role1, RoleTable role2) {
            this.roleDao = roleDao;
            this.role1 = role1;
            this.role2 = role2;
            this.id1 = id1;
            this.id2 = id2;
        }

        void setUp() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.insert(role1))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.insert(role2))));
        }

        void tearDown() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id2, uniOptionalHelper(roleDao.delete(id2))));
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(id1, uniOptionalHelper(roleDao.delete(id1))));
        }
    }

    @Nested
    @DisplayName("UserService")
    class UserServiceTest {

        RoleTableDaoHelper helper;

        Principal principal;
        RoleTable role1 = RoleTable.builder()
                .id(id1)
                .role(RoleTableDaoHelper.ROLE_1)
                .userName(ROOT)
                .build();
        RoleTable role2 = RoleTable.builder()
                .id(id2)
                .role(RoleTableDaoHelper.ROLE_2)
                .userName(ROOT)
                .build();
        User user = User.builder()
                .id(id0)
                .userName("user")
                .password("password")
                .roles(Collections.emptySet())
                .build();

        @BeforeEach
        void setUp() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count())));
            helper = new RoleTableDaoHelper(roleDao, id1, id2, role1, role2);
            helper.setUp();
        }

        @AfterEach
        void tearDown() {
            helper.tearDown();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count())));
        }

        @Test
        void test() {
            var expected1 = userServiceAddUser();
            deleteUserName();
            checkUserNameTable();
            for (var roles : new String[][]{{RoleTableDaoHelper.ROLE_1}, {RoleTableDaoHelper.ROLE_1, RoleTableDaoHelper.ROLE_2}}) {
                userServiceAddUserWithRoles(expected1, roles);
                checkUserNameTable();
            }
            var expected2 = Answer.builder()
                    .message(Answer.DEFAULT_MESSAGE)
                    .error(202)
                    .payload(new ApiResponse<>(TestData.uuid.ZERO, 202))
                    .build();
            for (var roles : new String[][]{{RoleTableDaoHelper.ROLE_1}, {RoleTableDaoHelper.ROLE_1, RoleTableDaoHelper.ROLE_2}}) {
                userServicePutUserWithRoles(expected1, expected2, roles);
                checkUserNameTable();
            }
        }

        @NotNull
        private Answer userServiceAddUser() {
            var expected = Answer.builder()
                    .message(Answer.DEFAULT_MESSAGE)
                    .error(201)
                    .payload(new ApiResponse<>(id0, 201))
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
            });
            return expected;
        }

        private void userServiceAddUserWithRoles(Answer expected, String[] roles) {
            var set = Set.of(roles);
            user = user.toBuilder().roles(set).build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
                var userView = uniOptionalHelper(userViewDao.findById(id0));
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
                        .payload(new ApiResponse<>(id0, 200))
                        .build();
                var actual = uniToAnswerHelper(userService.delete(new Request<>(user.id(), principal)));
                Assertions.assertNotNull(actual);
                Assertions.assertEquals(expected200, actual);
                if (actual.payload() instanceof ApiResponse apiResponse) {
                    Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                            id0, UUID.fromString(apiResponse.id().toString())
                    ));
                }
            });
        }

        private void userServicePutUserWithRoles(Answer expected, Answer expected2, String[] roles) {
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.add(new Request<>(user, principal)));
                Assertions.assertEquals(expected, actual);
            });
            var set = Set.of(roles);
            var first = set.stream().findFirst();
            Assertions.assertFalse(first.isEmpty());
            user = user.toBuilder().roles(set).build();
            Assertions.assertDoesNotThrow(() -> {
                var actual = uniToAnswerHelper(userService.put(new Request<>(user, principal)));
                Assertions.assertEquals(expected2, actual);
                var userView = uniOptionalHelper(userViewDao.findById(id0));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(set.size(), userView.roles().size());
            });
            userServiceDeleteByUserId();
        }

        private void checkUserNameTable() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count()))
            );
        }

        private void deleteUserName() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(
                    id0, uniOptionalHelper(userNameDao.delete(id0))
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
            super.whenFindRangeFromZeroToMaxValueThenMultiWithTwoItems(3);
        }
    }

    @Nested
    @DisplayName("UserTransactionalJob")
    class UserTransactionalJobTest {
        UUID customId;
        RoleTableDaoHelper helper;
        RoleTable role1 = RoleTable.builder()
                .id(id1)
                .role("role1")
                .userName("root")
                .build();
        RoleTable role2 = RoleTable.builder()
                .id(id2)
                .role("role2")
                .userName("root")
                .build();
        UserNameTable userName = UserNameTable.builder()
                .id(id0)
                .userName("user")
                .password("password")
                .enabled(true)
                .build();

        @BeforeEach
        void setUp() {
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count())));
            helper = new RoleTableDaoHelper(roleDao, id1, id2, role1, role2);
            helper.setUp();
        }

        @AfterEach
        void tearDown() {
            helper.tearDown();
            Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count())));
        }

        @Test
        void test() {
            userNameWithoutRolesInserted();
            deleteUserName();
            checkUserNameTable();
            userNameWithRoleROLE1Inserted();
            deleteUserName();
            checkUserNameTable();
            userNameWithRolesROLE1AndROLE2Inserted();
            userNameWithRoleROLE1Updated();
            deleteUserName();
            checkUserNameTable();
            userNameWithRolesROLE1AndROLE2AndROLE3TryInsertingGotExeption();
            checkUserNameTable();
            var custom = UserNameTable.builder()
                    .userName("userName")
                    .password("password")
                    .enabled(true)
                    .build();
            Assertions.assertDoesNotThrow(() -> {
                customId = uniOptionalHelper(userTransactionalJob.insert(custom, Set.of(RoleTableDaoHelper.ROLE_1, RoleTableDaoHelper.ROLE_2)));
            });
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(customId, uniOptionalHelper(userNameDao.delete(customId))));
        }

        private void userNameWithoutRolesInserted() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.emptySet()));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id0, test);
                var userView = uniOptionalHelper(userViewDao.findById(id0));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(0, userView.roles().size());
            });

        }

        private void userNameWithRoleROLE1Inserted() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Collections.singleton("role1")));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id0, test);
                var userView = uniOptionalHelper(userViewDao.findById(id0));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(1, userView.roles().size());
            });
        }

        private void userNameWithRolesROLE1AndROLE2Inserted() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2")));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id0, test);
                var userView = uniOptionalHelper(userViewDao.findById(id0));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(2, userView.roles().size());
            });
        }

        private void userNameWithRoleROLE1Updated() {
            Assertions.assertDoesNotThrow(() -> {
                var test = uniOptionalHelper(userTransactionalJob.update(userName, Set.of("role1")));
                Assertions.assertNotNull(test);
                Assertions.assertEquals(id0, test);
                var userView = uniOptionalHelper(userViewDao.findById(id0));
                Assertions.assertNotNull(userView);
                Assertions.assertEquals(1, userView.roles().size());
            });
        }

        private void userNameWithRolesROLE1AndROLE2AndROLE3TryInsertingGotExeption() {
            Assertions.assertThrows(java.util.concurrent.ExecutionException.class,
                    () -> uniOptionalHelper(userTransactionalJob.insert(userName, Set.of("role1", "role2", "role3"))));
        }

        private void checkUserNameTable() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(1, uniOptionalHelper(userNameDao.count())));
        }

        private void deleteUserName() {
            Assertions.assertDoesNotThrow(
                    () -> Assertions.assertEquals(id0, uniOptionalHelper(userNameDao.delete(id0))));
        }
    }
}
