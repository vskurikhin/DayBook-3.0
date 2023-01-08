/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.SessionTable;
import su.svn.daybook.models.domain.Session;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class SessionMapper extends AbstractMapper<UUID, Session, SessionTable> {

    private static final Logger LOG = Logger.getLogger(SessionMapper.class);

    protected SessionMapper() throws NoSuchMethodException {
        super(Session.class, Session::builder, SessionTable.class, SessionTable::builder, LOG);
    }

    @Override
    public SessionTable convertToDomain(Session model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Session convertToModel(SessionTable domain) {
        return super.convertDomainToModel(domain);
    }
}
