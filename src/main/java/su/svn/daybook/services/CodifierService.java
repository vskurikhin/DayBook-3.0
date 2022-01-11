/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.dao.CodifierDao;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Codifier;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CodifierService {

    private static final Logger LOG = Logger.getLogger(CodifierService.class);

    @Inject
    CodifierDao codifierDao;

    /**
     * This is method a Vertx message consumer and Codifier provider by id
     *
     * @param o - id of the Codifier
     * @return - a lazy asynchronous action with the Answer containing the Codifier as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODE_GET)
    public Uni<Answer> codeGet(Object o) {
        var methodCall = String.format("codeGet(%s)", o);
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
        return codifierDao.findById(id)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    /**
     * This is method a Vertx message consumer and Codifier creater
     *
     * @param o - Codifier
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODE_ADD)
    public Uni<Answer> codeAdd(Codifier o) {
        LOG.tracef("codeAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(Codifier entry) {
        return codifierDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Codifier updater
     *
     * @param o - Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODE_PUT)
    public Uni<Answer> codePut(Codifier o) {
        LOG.tracef("codePut(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(Codifier entry) {
        return codifierDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Codifier deleter
     *
     * @param o - id of the Codifier
     * @return - a LAA with the Answer containing Codifier id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.CODE_DEL)
    public Uni<Answer> codeDelete(Object o) {
        var methodCall = String.format("codeDelete(%s)", o);
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

    private Uni<Answer> delete(long id) {
        return codifierDao.delete(id)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse(o.get())));
    }

    /**
     * The method provides the Answer's flow with all entries of Codifier
     *
     * @return - the Answer's Multi-flow with all entries of Codifier
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return codifierDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }

    private Answer getAnswer(@Nonnull Codifier Codifier) {
        LOG.tracef("getAnswer Codifier: %s", Codifier);
        return Answer.of(Codifier);
    }
}
