/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Identification.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models;

import java.io.Serializable;

public interface Identification<I extends Comparable<? extends Serializable>> {
    I id();
}
