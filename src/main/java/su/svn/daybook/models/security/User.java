package su.svn.daybook.models.security;

import org.jboss.logging.Logger;
import su.svn.daybook.domain.model.UserView;

import java.util.Set;

@Deprecated
public record User(String username, String password, Set<String> roles) {

    private static final Logger LOG = Logger.getLogger(User.class);

    @Deprecated
    public static User from(UserView userView) {
        return new User(userView.userName(), userView.password(), userView.roles());
    }
}
