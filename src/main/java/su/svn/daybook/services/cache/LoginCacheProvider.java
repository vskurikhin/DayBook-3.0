/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.Logged;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.services.domain.LoginDataService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class LoginCacheProvider extends AbstractCacheProvider<String, UserView> {

    private static final Logger LOG = Logger.getLogger(LoginCacheProvider.class);

    @Inject
    LoginDataService loginDataService;

    public LoginCacheProvider() {
        super(EventAddress.LOGIN_REQUEST, null, LOG);
    }

    @PostConstruct
    public void setup() {
        super.setup(String.class, UserView.class);
    }

    @Logged
    @Counted
    @CacheResult(cacheName = EventAddress.LOGIN_REQUEST)
    public Uni<UserView> get(@CacheKey String userName) {
        return loginDataService.findByUserName(userName);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateByKey(String userName, Answer answer) {
        return invalidateCacheByKey(userName).map(l -> answer);
    }

    @Logged
    public Uni<Answer> invalidateById(UUID id, Answer answer) {
        return super.invalidateByOther(id, answer, UserView::id, UserView::userName);
    }
}
