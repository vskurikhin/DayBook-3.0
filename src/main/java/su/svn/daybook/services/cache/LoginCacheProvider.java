/*
 * This file was last modified at 2023.11.20 00:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LoginCacheProvider.java
 * $Id$
 */

package su.svn.daybook.services.cache;

import io.micrometer.core.annotation.Counted;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import su.svn.daybook.annotations.PrincipalLogging;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserView;
import su.svn.daybook.services.domain.LoginDataService;

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

    @Counted
    @PrincipalLogging
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

    @PrincipalLogging
    public Uni<Answer> invalidateById(UUID id, Answer answer) {
        return super.invalidateByOther(id, answer, UserView::id, UserView::userName);
    }
}
