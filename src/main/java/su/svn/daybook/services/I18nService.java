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
import su.svn.daybook.domain.dao.I18nDao;
import su.svn.daybook.domain.enums.EventAddress;
import su.svn.daybook.domain.messages.Answer;
import su.svn.daybook.domain.model.I18n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class I18nService {

    private static final Logger LOG = Logger.getLogger(I18nService.class);

    @Inject
    I18nDao i18nDao;

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

    @ConsumeEvent(EventAddress.I18N_ADD)
    public Uni<Answer> i18nAdd(I18n o) {
        LOG.tracef("i18nAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(I18n entry) {
        return i18nDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(o.get()));
    }

    @ConsumeEvent(EventAddress.I18N_PUT)
    public Uni<Answer> i18nPut(I18n o) {
        LOG.tracef("i18nAdd(%s)", o);
        return put(o);
    }

    private Uni<Answer> put(I18n entry) {
        return i18nDao.update(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(o.get()));
    }

    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return i18nDao.findAll()
                .onItem()
                .transform(this::getAnswer);
    }

    private Answer getAnswer(I18n I18n) {
        LOG.infof("getAnswer %s", I18n);
        if (I18n != null) {
            LOG.tracef("getAnswer I18n: %s", I18n);
            return Answer.of(I18n);
        } else  {
            LOG.trace("getAnswer I18n: is null");
            return Answer.empty();
        }
    }
}
