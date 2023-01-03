package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.ValueTypeDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.ValueTypeTable;
import su.svn.daybook.models.domain.ValueType;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.ValueTypeMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ValueTypeCacheProvider extends AbstractCacheProvider<Long> {

    private static final Logger LOG = Logger.getLogger(ValueTypeCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    ValueTypeDao valueTypeDao;

    @Inject
    ValueTypeMapper valueTypeMapper;

    public ValueTypeCacheProvider() {
        super(EventAddress.VALUE_TYPE_GET, EventAddress.VALUE_TYPE_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.VALUE_TYPE_GET)
    public Uni<ValueType> get(@CacheKey Long id) {
        LOG.tracef("get(%s)", id);
        return valueTypeDao
                .findById(id)
                .map(Optional::get)
                .map(valueTypeMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.VALUE_TYPE_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, valueTypeDao::count, valueTypeDao::findRange, this::answerOfModel);
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

    private Answer answerOfModel(ValueTypeTable table) {
        return Answer.of(valueTypeMapper.convertToModel(table));
    }
}
