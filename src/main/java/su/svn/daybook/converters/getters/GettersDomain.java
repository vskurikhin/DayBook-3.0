package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.DomainCollector;
import su.svn.daybook.converters.records.FieldRecord;

import java.io.Serializable;
import java.util.Map;

public class GettersDomain<P extends Serializable> extends AbstractGetters<P> implements DomainCollector<P> {

    public GettersDomain(Class<P> pClass) {
        super(pClass);
    }

    @Override
    public Map<String, FieldRecord> collectFields(Class<P> pClass) {
        return collectDomainFields(pClass);
    }
}
