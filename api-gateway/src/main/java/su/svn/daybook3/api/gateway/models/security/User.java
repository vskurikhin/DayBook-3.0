/*
 * This file was last modified at 2024-05-14 19:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * User.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.security;

import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.domain.model.UserView;

import java.util.Set;

@Deprecated
public record User(String username, String password, Set<String> roles) {

    private static final Logger LOG = Logger.getLogger(User.class);

    @Deprecated
    public static User from(UserView userView) {
        return new User(userView.userName(), userView.password(), userView.roles());
    }
}
