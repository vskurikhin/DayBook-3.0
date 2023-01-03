package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.CodifierTable;
import su.svn.daybook.models.domain.Codifier;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.CodifierMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class CodifierCacheProvider extends AbstractCacheProvider<String> {

    private static final Logger LOG = Logger.getLogger(CodifierCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    CodifierDao codifierDao;

    @Inject
    CodifierMapper codifierMapper;

    public CodifierCacheProvider() {
        super(EventAddress.CODIFIER_GET, EventAddress.CODIFIER_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.CODIFIER_GET)
    public Uni<Codifier> get(@CacheKey String id) {
        LOG.tracef("get(%s)", id);
        return codifierDao
                .findById(id)
                .map(Optional::get)
                .map(codifierMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.CODIFIER_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, codifierDao::count, codifierDao::findRange, this::answerOfModel);
    }

    @Override
    public Uni<Answer> invalidate(Answer answer) {
        return invalidateAllPagesCache(answer);
    }

    @Override
    public Uni<Answer> invalidateById(String id, Answer answer) {
        return invalidateCacheById(id).map(l -> answer);
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    private Answer answerOfModel(CodifierTable table) {
        return Answer.of(codifierMapper.convertToModel(table));
    }
}
