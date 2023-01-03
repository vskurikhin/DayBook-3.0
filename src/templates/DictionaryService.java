/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Service.java
 * $Id$
 */

package su.svn.daybook.services.domain;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.@Name@Dao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.models.domain.@Name@;
import su.svn.daybook.models.pagination.Page;
import su.svn.daybook.models.pagination.PageRequest;
import su.svn.daybook.services.ExceptionAnswerService;
import su.svn.daybook.services.cache.@Name@CacheProvider;
import su.svn.daybook.services.mappers.@Name@Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;

@ApplicationScoped
public class @Name@Service extends AbstractService<@IdType@, @Name@> {

    private static final Logger LOG = Logger.getLogger(@Name@Service.class);

    @Inject
    @Name@CacheProvider @name@CacheProvider;

    @Inject
    @Name@Dao @name@Dao;

    @Inject
    @Name@Mapper @name@Mapper;

    @Inject
    ExceptionAnswerService exceptionAnswerService;

    /**
     * This is method a Vertx message consumer and @Name@ provider by id
     *
     * @param o - id of the @Name@
     * @return - a lazy asynchronous action with the Answer containing the @Name@ as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_GET)
    public Uni<Answer> get(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("get(%s)", o);
        try {
            return getEntry(getId@IdType@(o));
        } catch (NumberFormatException e) {
            LOG.errorf("get(%s)", o, e);
            return exceptionAnswerService.getUniAnswerNoNumber(e);
        } catch (NoSuchElementException e) {
            LOG.errorf("get(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    /**
     * The method provides the Answer's flow with all entries of @Name@
     *
     * @return - the Answer's Multi-flow with all entries of @Name@
     */
    public Multi<Answer> getAll() {
        //noinspection DuplicatedCode
        LOG.trace("getAll()");
        return @name@Dao
                .count()
                .onItem()
                .transformToMulti(count -> getAllIfNotOverSize(count, this::getAllModels));
    }

    private Multi<@Name@> getAllModels() {
        return @name@Dao
                .findAll()
                .map(@name@Mapper::convertToModel);
    }

    private Uni<Answer> getEntry(@IdType@ id) {
        return @name@CacheProvider
                .get(id)
                .map(Answer::of)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    @ConsumeEvent(EventAddress.@TABLE@_PAGE)
    public Uni<Page<Answer>> getPage(PageRequest pageRequest) {
        //noinspection DuplicatedCode
        LOG.tracef("getPage(%s)", pageRequest);
        return @name@CacheProvider.getPage(pageRequest);
    }

    /**
     * This is method a Vertx message consumer and @Name@ creater
     *
     * @param o - @Name@
     * @return - a lazy asynchronous action (LAA) with the Answer containing the @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_ADD)
    public Uni<Answer> add(@Name@ o) {
        //noinspection DuplicatedCode
        LOG.tracef("add(%s)", o);
        return addEntry(@name@Mapper.convertToDomain(o));
    }

    private Uni<Answer> addEntry(@Name@Table entry) {
        return @name@Dao
                .insert(entry)
                .map(o -> apiResponseWith@Key@Answer(201, o))
                .flatMap(@name@CacheProvider::invalidate)
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and @Name@ updater
     *
     * @param o - @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_PUT)
    public Uni<Answer> put(@Name@ o) {
        //noinspection DuplicatedCode
        LOG.tracef("put(%s)", o);
        return putEntry(@name@Mapper.convertToDomain(o));
    }

    private Uni<Answer> putEntry(@Name@Table entry) {
        return @name@Dao
                .update(entry)
                .flatMap(this::apiResponseAcceptedUniAnswer)
                .flatMap(answer -> @name@CacheProvider.invalidateById(entry.getId(), answer))
                .onFailure(exceptionAnswerService::testDuplicateException)
                .recoverWithUni(exceptionAnswerService::notAcceptableDuplicateAnswer)
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(get(entry.getId()))
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }

    /**
     * This is method a Vertx message consumer and @Name@ deleter
     *
     * @param o - id of the @Name@
     * @return - a LAA with the Answer containing @Name@ id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.@TABLE@_DEL)
    public Uni<Answer> delete(Object o) {
        //noinspection DuplicatedCode
        LOG.tracef("delete(%s)", o);
        try {
            return deleteEntry(getId@IdType@(o));
        } catch (NumberFormatException e) {
            LOG.errorf("delete(%s)", o, e);
            return exceptionAnswerService.getUniAnswerNoNumber(e);
        } catch (NoSuchElementException e) {
            LOG.errorf("delete(%s)", o, e);
            return Uni.createFrom().item(Answer.empty());
        }
    }

    private Uni<Answer> deleteEntry(@IdType@ id) {
        return @name@Dao
                .delete(id)
                .map(this::apiResponseWith@Key@Answer)
                .flatMap(answer -> @name@CacheProvider.invalidateById(id, answer))
                .onFailure(exceptionAnswerService::testNoSuchElementException)
                .recoverWithUni(exceptionAnswerService::noSuchElementAnswer)
                .onFailure(exceptionAnswerService::testException)
                .recoverWithUni(exceptionAnswerService::badRequestUniAnswer);
    }
}
