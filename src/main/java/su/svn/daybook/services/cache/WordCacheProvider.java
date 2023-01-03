package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.WordDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.WordTable;
import su.svn.daybook.models.domain.Word;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.WordMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class WordCacheProvider extends AbstractCacheProvider<String> {

    private static final Logger LOG = Logger.getLogger(WordCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    WordDao wordDao;

    @Inject
    WordMapper wordMapper;

    public WordCacheProvider() {
        super(EventAddress.WORD_GET, EventAddress.WORD_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.WORD_GET)
    public Uni<Word> get(@CacheKey String id) {
        LOG.tracef("get(%s)", id);
        return wordDao
                .findById(id)
                .map(Optional::get)
                .map(wordMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.WORD_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, wordDao::count, wordDao::findRange, this::answerOfModel);
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

    private Answer answerOfModel(WordTable table) {
        return Answer.of(wordMapper.convertToModel(table));
    }
}
