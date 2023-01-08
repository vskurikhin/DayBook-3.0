package su.svn.daybook.models.security;

import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.Set;

public record User(String username, String password, Set<String> roles) {

    private static final Logger LOG = Logger.getLogger(User.class);

    // this is just an example, you can load the user from the database (via PanacheEntityBase)
    public static User findByUsername(String username) {
        LOG.infof("findByUsername(%s)", username);

        //if using Panache pattern (extends or PanacheEntity PanacheEntityBase)
        //return find("username", username).firstResult();

        String userUsername = "user";

        //generated from password encoder
        String userPassword = "CPR39uiHa7Jv8edQX/gtEnNo+d0bX7TUZwHyqcScJVg=";

        String adminUsername = "admin";

        //generated from password encoder
        String adminPassword = "TctZzhFsjhNVUXytmohavKPhUNh5gfQAza4MJ+FFvDs=";

        if (username.equals(userUsername)) {
            LOG.infof("findByUsername(%s)", username);
            return new User(userUsername, userPassword, Collections.singleton("USER"));
        } else if (username.equals(adminUsername)) {
            LOG.infof("findByUsername(%s)", username);
            return new User(adminUsername, adminPassword, Collections.singleton("ADMIN"));
        } else {
            return null;
        }
    }
}
