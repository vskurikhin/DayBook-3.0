package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.models.domain.I18n;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.I18nMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class I18nCacheProvider extends AbstractCacheProvider<Long> {

    private static final Logger LOG = Logger.getLogger(I18nCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    I18nDao i18nDao;

    @Inject
    I18nMapper i18nMapper;

    public I18nCacheProvider() {
        super(EventAddress.I18N_GET, EventAddress.I18N_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.I18N_GET)
    public Uni<I18n> get(@CacheKey Long id) {
        LOG.tracef("get(%s)", id);
        return i18nDao
                .findById(id)
                .map(Optional::get)
                .map(i18nMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.I18N_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, i18nDao::count, i18nDao::findRange, this::answerOfModel);
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

    private Answer answerOfModel(I18nTable table) {
        return Answer.of(i18nMapper.convertToModel(table));
    }
}
