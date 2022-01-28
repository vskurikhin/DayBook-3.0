/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.I18n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class I18nService {

    private static final Logger LOG = Logger.getLogger(I18nService.class);

    @Inject
    I18nDao i18nDao;

    /**
     * This is method a Vertx message consumer and I18n provider by id
     *
     * @param o - id of the I18n
     * @return - a lazy asynchronous action with the Answer containing the I18n as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_GET)
    public Uni<Answer> i18nGet(Object o) {
        var methodCall = String.format("i18nGet(%s)", o);
        if (o instanceof String) {
            try {
                return get(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                LOG.error(methodCall, e);
                Answer numberError = new Answer(e.getMessage(), 404);
                return Uni.createFrom().item(numberError);
            }
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> get(Long id) {
        return i18nDao.findById(id)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    /**
     * This is method a Vertx message consumer and I18n creater
     *
     * @param o - I18n
     * @return - a lazy asynchronous action (LAA) with the Answer containing the I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_ADD)
    public Uni<Answer> i18nAdd(I18n o) {
        LOG.tracef("i18nAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(I18n entry) {
        return i18nDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and I18n updater
     *
     * @param o - I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_PUT)
    public Uni<Answer> i18nPut(I18n o) {
        LOG.tracef("i18nAdd(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(I18n entry) {
        return i18nDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * The method provides the Answer's flow with all entries of I18n
     *
     * @return - the Answer's Multi-flow with all entries of I18n
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return i18nDao.findAll()
                .onItem()
                .transform(Answer::of);
    }

    /**
     * This is method a Vertx message consumer and I18n deleter
     *
     * @param o - id of the I18n
     * @return - a LAA with the Answer containing I18n id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.I18N_DEL)
    public Uni<Answer> i18nDelete(Object o) {
        var methodCall = String.format("i18nDelete(%s)", o);
        if (o instanceof String) {
            try {
                return delete(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                LOG.error(methodCall, e);
                Answer numberError = new Answer(e.getMessage(), 404);
                return Uni.createFrom().item(numberError);
            }
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> delete(Long id) {
        return i18nDao.delete(id)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }
}
