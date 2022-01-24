/*
 * This file was last modified at 2022.01.11 17:44 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageService.java
 * $Id$
 */

package su.svn.daybook.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;
import su.svn.daybook.domain.dao.LanguageDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.messages.ApiResponse;
import su.svn.daybook.domain.model.Language;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LanguageService {

    private static final Logger LOG = Logger.getLogger(LanguageService.class);

    @Inject
    LanguageDao languageDao;

    /**
     * This is method a Vertx message consumer and Language provider by id
     *
     * @param o - id of the Language
     * @return - a lazy asynchronous action with the Answer containing the Language as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_GET)
    public Uni<Answer> languageGet(Object o) {
        var methodCall = String.format("languageGet(%s)", o);
        if (o instanceof String) {
            try {
                return get(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                LOG.error(methodCall, e);
                var numberError = new Answer(e.getMessage(), 404);
                return Uni.createFrom().item(numberError);
            }
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> get(Long id) {
        return languageDao.findById(id)
                .map(t -> t.isEmpty() ? Answer.empty() : Answer.of(t));
    }

    /**
     * This is method a Vertx message consumer and Language creater
     *
     * @param o - Language
     * @return - a lazy asynchronous action (LAA) with the Answer containing the Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_ADD)
    public Uni<Answer> languageAdd(Language o) {
        LOG.tracef("languageAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(Language entry) {
        return languageDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Language updater
     *
     * @param o - Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_PUT)
    public Uni<Answer> languagePut(Language o) {
        LOG.tracef("languagePut(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(Language entry) {
        return languageDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * This is method a Vertx message consumer and Language deleter
     *
     * @param o - id of the Language
     * @return - a LAA with the Answer containing Language id as payload or empty payload
     */
    @ConsumeEvent(EventAddress.LANGUAGE_DEL)
    public Uni<Answer> languageDelete(Object o) {
        var methodCall = String.format("languageDelete(%s)", o);
        if (o instanceof String) {
            try {
                return delete(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                LOG.error(methodCall, e);
                var numberError = new Answer(e.getMessage(), 404);
                return Uni.createFrom().item(numberError);
            }
        }
        return Uni.createFrom().item(Answer.empty());
    }

    private Uni<Answer> delete(long id) {
        return languageDao.delete(id)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(new ApiResponse<>(o.get())));
    }

    /**
     * The method provides the Answer's flow with all entries of Language
     *
     * @return - the Answer's Multi-flow with all entries of Language
     */
    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return languageDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }

    private Answer getAnswer(@Nonnull Language Language) {
        LOG.tracef("getAnswer Language: %s", Language);
        return Answer.of(Language);
    }
}
