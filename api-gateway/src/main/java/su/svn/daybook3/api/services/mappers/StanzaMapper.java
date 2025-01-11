/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaMapper.java
 * $Id$
 */

package su.svn.daybook3.api.services.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.domain.model.StanzaTable;
import su.svn.daybook3.api.domain.model.StanzaView;
import su.svn.daybook3.api.models.domain.Stanza;
import su.svn.daybook3.converters.mappers.AbstractMapper;

@ApplicationScoped
public class StanzaMapper extends AbstractMapper<Long, Stanza, StanzaView> {

    private static final Logger LOG = Logger.getLogger(StanzaMapper.class);

    private final StanzaMapper.SettingTableMapper settingTableMapper;

    protected StanzaMapper() throws NoSuchMethodException {
        super(Stanza.class, Stanza::builder, StanzaView.class, StanzaView::builder, LOG);
        this.settingTableMapper = new StanzaMapper.SettingTableMapper();
    }

    @Override
    public StanzaView convertToDomain(Stanza model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Stanza convertToModel(StanzaView domain) {
        return super.convertDomainToModel(domain);
    }

    public StanzaTable convertToSettingTable(Stanza model) {
        return settingTableMapper.convertToDomain(model);
    }

    public static class SettingTableMapper extends AbstractMapper<Long, Stanza, StanzaTable> {

        private static final Logger LOG = Logger.getLogger(SettingMapper.SettingTableMapper.class);

        protected SettingTableMapper() throws NoSuchMethodException {
            super(Stanza.class, Stanza::builder, StanzaTable.class, StanzaTable::builder, LOG);
        }

        @Override
        public StanzaTable convertToDomain(Stanza model) {
            return super.convertModelToDomain(model);
        }

        @Override
        public Stanza convertToModel(StanzaTable domain) {
            return super.convertDomainToModel(domain);
        }
    }
}
