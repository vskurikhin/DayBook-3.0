/*
 * This file was last modified at 2023.01.07 11:52 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserDataService.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.dao.UserViewDao;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.domain.transact.UserTransactionalJob;
import su.svn.daybook.models.domain.User;
import su.svn.daybook.services.mappers.UserMapper;
import su.svn.daybook.services.security.PBKDF2Encoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class UserDataService implements DataService<UUID, UserView, User> {

    private static final Logger LOG = Logger.getLogger(UserDataService.class);

    @Inject
    UserNameDao userNameDao;

    @Inject
    UserViewDao userViewDao;

    @Inject
    UserMapper userMapper;

    @Inject
    UserTransactionalJob userTransactionalJob;

    @Inject
    PBKDF2Encoder passwordEncoder;

    public Uni<UUID> add(User o) {
        LOG.tracef("add(%s)", o);
        return addUserAndRoles(userMapper.convertToUserNameTable(passwordEncoding(o)), o.getRoles());
    }

    private Uni<UUID> addUserAndRoles(UserNameTable entry, Set<String> roles) {
        return userTransactionalJob
                .insert(entry, roles, UserNameTable::getUserName)
                .map(o -> lookup(o, entry));
    }

    public Uni<Long> count() {
        return userViewDao
                .count()
                .map(o -> lookupLong(o, "count for UserView"));
    }

    public Multi<User> findRange(long offset, long limit) {
        LOG.tracef("findRange(%d, %d)", offset, limit);
        return userViewDao
                .findRange(offset, limit)
                .map(userMapper::convertToModel);
    }

    public Uni<User> get(UUID id) {
        LOG.tracef("get(%s)", id);
        return userViewDao
                .findById(id)
                .map(o -> lookup(o, id))
                .map(userMapper::convertToModel);
    }

    public Multi<User> getAll() {
        LOG.tracef("getAll()");
        return userViewDao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, userViewDao::findAll))
                .map(userMapper::convertToModel);
    }

    public Uni<UUID> put(User o) {
        LOG.tracef("put(%s)", o);
        return putEntry(userMapper.convertToUserNameTable(passwordEncoding(o)), o.getRoles());
    }

    private Uni<UUID> putEntry(UserNameTable entry, Set<String> roles) {
        return userTransactionalJob
                .update(entry, roles, UserNameTable::getUserName)
                .map(o -> lookup(o, entry));
    }

    private User passwordEncoding(User o) {
        return User
                .builder()
                .id(o.getId())
                .userName(o.getUserName())
                .password(passwordEncoder.encode(o.getPassword()))
                .roles(o.getRoles())
                .build();
    }

    public Uni<UUID> delete(UUID id) {
        LOG.tracef("delete(%s)", id);
        return deleteEntry(id);
    }

    private Uni<UUID> deleteEntry(UUID id) {
        return userNameDao
                .findById(id)
                .map(o -> lookup(o, id))
                .flatMap(this::deleteEntry);
    }

    private Uni<UUID> deleteEntry(UserNameTable o) {
        return userTransactionalJob
                .delete(o, UserNameTable::getUserName)
                .map(i -> lookup(i, o));
    }
}
