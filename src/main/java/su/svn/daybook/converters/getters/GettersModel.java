package su.svn.daybook.converters.getters;

import su.svn.daybook.converters.ModelCollector;
import su.svn.daybook.converters.records.FieldRecord;

import java.io.Serializable;
import java.util.Map;

public class GettersModel<P extends Serializable> extends AbstractGetters<P> implements ModelCollector<P> {

    public GettersModel(Class<P> pClass) {
        super(pClass);
    }

    @Override
    public Map<String, FieldRecord> collectFields(Class<P> pClass) {
        return collectModelFields(pClass);
    }
}
