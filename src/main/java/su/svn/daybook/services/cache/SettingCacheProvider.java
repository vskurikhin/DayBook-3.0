package su.svn.daybook.services.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.SettingDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.SettingTable;
import su.svn.daybook.models.domain.Setting;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.PageService;
import su.svn.daybook.services.mappers.SettingMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class SettingCacheProvider extends AbstractCacheProvider<Long> {

    private static final Logger LOG = Logger.getLogger(SettingCacheProvider.class);

    @Inject
    CacheManager cacheManager;

    @Inject
    PageService pageService;

    @Inject
    SettingDao settingDao;

    @Inject
    SettingMapper settingMapper;

    public SettingCacheProvider() {
        super(EventAddress.SETTING_GET, EventAddress.SETTING_PAGE, LOG);
    }

    @CacheResult(cacheName = EventAddress.SETTING_GET)
    public Uni<Setting> get(@CacheKey Long id) {
        LOG.tracef("get(%s)", id);
        return settingDao
                .findById(id)
                .map(Optional::get)
                .map(settingMapper::convertToModel);
    }

    @CacheResult(cacheName = EventAddress.SETTING_PAGE)
    public Uni<Page<Answer>> getPage(@CacheKey PageRequest pageRequest) {
        LOG.tracef("getPage(%s)", pageRequest);
        return pageService.getPage(pageRequest, settingDao::count, settingDao::findRange, this::answerOfModel);
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

    private Answer answerOfModel(SettingTable table) {
        return Answer.of(settingMapper.convertToModel(table));
    }
}
