/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * User.java
 * $Id$
 */

package su.svn.daybook3.auth.models.security;

import org.jboss.logging.Logger;
import su.svn.daybook3.auth.domain.model.UserView;

import java.util.Set;

@Deprecated
public record User(String username, String password, Set<String> roles) {

    private static final Logger LOG = Logger.getLogger(User.class);

    @Deprecated
    public static User from(UserView userView) {
        return new User(userView.userName(), userView.password(), userView.roles());
    }
}
