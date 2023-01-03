package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.TagLabelDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.TagLabelTable;
import su.svn.daybook.models.domain.TagLabel;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.TagLabelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class TagLabelCacheProvider extends AbstractCacheProvider<String> {

    private static final Logger LOG = Logger.getLogger(TagLabelCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    TagLabelDao tagLabelDao;

    @Inject
    TagLabelMapper tagLabelMapper;

    public TagLabelCacheProvider() {
        super(EventAddress.TAG_LABEL_GET, EventAddress.TAG_LABEL_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.TAG_LABEL_GET)
    public Uni<TagLabel> get(@CacheKey String id) {
        LOG.tracef("get(%s)", id);
        return tagLabelDao
                .findById(id)
                .map(Optional::get)
                .map(tagLabelMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.TAG_LABEL_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, tagLabelDao::count, tagLabelDao::findRange, this::answerOfModel);
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

    private Answer answerOfModel(TagLabelTable table) {
        return Answer.of(tagLabelMapper.convertToModel(table));
    }
}
