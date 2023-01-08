/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.models.domain.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class UserMapper extends AbstractMapper<UUID, User, UserView> {

    private static final Logger LOG = Logger.getLogger(UserMapper.class);

    private final UserAndUserNameTableMapper delegate;

    protected UserMapper() throws NoSuchMethodException {
        super(User.class, User::builder, UserView.class, UserView::builder, LOG);
        this.delegate = new UserAndUserNameTableMapper();
    }

    @Override
    public UserView convertToDomain(User model) {
        return super.convertModelToDomain(model);
    }

    public UserNameTable convertToUserNameTable(User model) {
        return delegate.convertToDomain(model);
    }

    @Override
    public User convertToModel(UserView domain) {
        return super.convertDomainToModel(domain);
    }

    public static class UserAndUserNameTableMapper extends AbstractMapper<UUID, User, UserNameTable> {

        protected UserAndUserNameTableMapper() throws NoSuchMethodException {
            super(User.class, User::builder, UserNameTable.class, UserNameTable::builder, LOG);
        }

        @Override
        public UserNameTable convertToDomain(User model) {
            return super.convertModelToDomain(model);
        }

        @Override
        public User convertToModel(UserNameTable domain) {
            return super.convertDomainToModel(domain);
        }
    }
}
