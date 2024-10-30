/*
 * This file was last modified at 2024-10-29 23:33 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Identification.java
 * $Id$
 */

package su.svn.daybook3.models;

import java.io.Serializable;

public interface Identification<I extends Comparable<? extends Serializable>> {
    I id();
}
