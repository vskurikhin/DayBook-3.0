package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.UserNameDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.models.domain.UserName;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.UserNameMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserNameCacheProvider extends AbstractCacheProvider<UUID> {

    private static final Logger LOG = Logger.getLogger(UserNameCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    UserNameDao wordDao;

    @Inject
    UserNameMapper wordMapper;

    public UserNameCacheProvider() {
        super(EventAddress.USER_GET, EventAddress.USER_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.USER_GET)
    public Uni<UserName> get(@CacheKey UUID id) {
        LOG.tracef("get(%s)", id);
        return wordDao
                .findById(id)
                .map(Optional::get)
                .map(wordMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.USER_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, wordDao::count, wordDao::findRange, this::answerOfModel);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(UUID id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    private Answer answerOfModel(UserNameTable table) {
        return Answer.of(wordMapper.convertToModel(table));
    }
}
