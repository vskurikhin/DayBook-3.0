/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.converters.mappers.AbstractMapper;
import su.svn.daybook3.api.gateway.domain.model.SessionTable;
import su.svn.daybook3.api.gateway.models.domain.Session;

import jakarta.enterprise.context.ApplicationScoped;

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
