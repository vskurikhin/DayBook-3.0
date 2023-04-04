/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nViewMapper.java
 * $Id$
 */

package su.svn.daybook.services.mappers;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.mappers.AbstractMapper;
import su.svn.daybook.domain.model.I18nTable;
import su.svn.daybook.domain.model.I18nView;
import su.svn.daybook.models.domain.I18n;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class I18nMapper extends AbstractMapper<Long, I18n, I18nView> {

    private static final Logger LOG = Logger.getLogger(I18nMapper.class);

    private I18nTableMapper i18nTableMapper;

    protected I18nMapper() throws NoSuchMethodException {
        super(I18n.class, I18n::builder, I18nView.class, I18nView::builder, LOG);
        this.i18nTableMapper = new I18nTableMapper();
    }

    @Override
    public I18nView convertToDomain(I18n model) {
        return super.convertModelToDomain(model);
    }

    public I18nTable convertToI18nTable(I18n model) {
        return i18nTableMapper.convertToDomain(model);
    }

    @Override
    public I18n convertToModel(I18nView domain) {
        return super.convertDomainToModel(domain);
    }

    public static class I18nTableMapper extends AbstractMapper<Long, I18n, I18nTable> {

        private static final Logger LOG = Logger.getLogger(su.svn.daybook.services.mappers.I18nMapper.class);

        protected I18nTableMapper() throws NoSuchMethodException {
            super(I18n.class, I18n::builder, I18nTable.class, I18nTable::builder, LOG);
        }

        @Override
        public I18nTable convertToDomain(I18n model) {
            return super.convertModelToDomain(model);
        }

        @Override
        public I18n convertToModel(I18nTable domain) {
            return super.convertDomainToModel(domain);
        }
    }
}
