/*
 * This file was last modified at 2024-05-14 23:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingMapper.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook3.api.gateway.converters.mappers.AbstractMapper;
import su.svn.daybook3.api.gateway.domain.model.SettingView;
import su.svn.daybook3.api.gateway.domain.model.SettingTable;
import su.svn.daybook3.api.gateway.models.domain.Setting;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SettingMapper extends AbstractMapper<Long, Setting, SettingView> {

    private static final Logger LOG = Logger.getLogger(SettingMapper.class);

    private final SettingTableMapper settingTableMapper;

    protected SettingMapper() throws NoSuchMethodException {
        super(Setting.class, Setting::builder, SettingView.class, SettingTable::builder, LOG);
        this.settingTableMapper = new SettingTableMapper();
    }

    @Override
    public SettingView convertToDomain(Setting model) {
        return super.convertModelToDomain(model);
    }

    @Override
    public Setting convertToModel(SettingView domain) {
        return super.convertDomainToModel(domain);
    }

    public SettingTable convertToSettingTable(Setting model) {
        return settingTableMapper.convertToDomain(model);
    }

    public static class SettingTableMapper extends AbstractMapper<Long, Setting, SettingTable> {

        private static final Logger LOG = Logger.getLogger(SettingTableMapper.class);

        protected SettingTableMapper() throws NoSuchMethodException {
            super(Setting.class, Setting::builder, SettingTable.class, SettingTable::builder, LOG);
        }

        @Override
        public SettingTable convertToDomain(Setting model) {
            return super.convertModelToDomain(model);
        }

        @Override
        public Setting convertToModel(SettingTable domain) {
            return super.convertDomainToModel(domain);
        }
    }
}
