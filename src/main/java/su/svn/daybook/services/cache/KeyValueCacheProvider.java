package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.KeyValueDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.KeyValueTable;
import su.svn.daybook.models.domain.KeyValue;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.KeyValueMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class KeyValueCacheProvider extends AbstractCacheProvider<Long> {

    private static final Logger LOG = Logger.getLogger(KeyValueCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    KeyValueDao keyValueDao;

    @Inject
    KeyValueMapper keyValueMapper;

    public KeyValueCacheProvider() {
        super(EventAddress.KEY_VALUE_GET, EventAddress.KEY_VALUE_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.KEY_VALUE_GET)
    public Uni<KeyValue> get(@CacheKey Long id) {
        LOG.tracef("get(%s)", id);
        return keyValueDao
                .findById(id)
                .map(Optional::get)
                .map(keyValueMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.KEY_VALUE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, keyValueDao::count, keyValueDao::findRange, this::answerOfModel);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(Long id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    private Answer answerOfModel(KeyValueTable table) {
        return Answer.of(keyValueMapper.convertToModel(table));
    }
}
