package su.svn.daybook.models;

import java.io.Serializable;

public interface Identification<I extends Comparable<? extends Serializable>> {
    I id();
}
