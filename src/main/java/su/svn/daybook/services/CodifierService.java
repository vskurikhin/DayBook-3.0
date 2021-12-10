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
import su.svn.daybook.domain.model.Codifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CodifierService {

    private static final Logger LOG = Logger.getLogger(CodifierService.class);

    @Inject
    CodifierDao codifierDao;

    @ConsumeEvent(EventAddress.CODE_GET)
    public Uni<Answer> codeGet(Object o) {
        var methodCall = String.format("tagGet(%s)", o);
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

    @ConsumeEvent(EventAddress.CODE_ADD)
    public Uni<Answer> codeAdd(Codifier o) {
        LOG.tracef("tagAdd(%s)", o);
        return add(o);
    }

    private Uni<Answer> add(Codifier entry) {
        return codifierDao.insert(entry)
                .map(o -> o.isEmpty() ? Answer.empty() : Answer.of(o.get()));
    }

    public Multi<Answer> getAll() {
        LOG.trace("getAll");
        return codifierDao.findAll()
                .onItem()
                .transform(this::getTagLabelAnswer);
    }

    private Answer getTagLabelAnswer(Codifier Codifier) {
        LOG.infof("getTagLabelAnswer %s", Codifier);
        if (Codifier != null) {
            LOG.tracef("getTagLabelAnswerFunction Codifier: %s", Codifier);
            return Answer.of(Codifier);
        } else  {
            LOG.trace("getTagLabelAnswerFunction Codifier: is null");
            return Answer.empty();
        }
    }
}
