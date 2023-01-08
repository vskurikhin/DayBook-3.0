/*
 * This file was last modified at 2023.01.08 12:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PageRequest.java
 * $Id$
 */

package su.svn.daybook.models.pagination;

import java.io.Serial;
import java.io.Serializable;

public final class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6635074121314255123L;

    public static final long UPPER_BOUND = 70366596661249L;

    private final long pageNumber;
    private final short limit;

    PageRequest() {
        this(0L, (short) 0);
    }

    public PageRequest(long pageNumber, short limit) {
        this.pageNumber = pageNumber;
        this.limit = limit;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public short getLimit() {
        return limit;
    }

    public long calculateTotalPages(long totalElements) {
        var result = (double) totalElements / limit;
        if (Double.isFinite(result)) {
            return (long) Math.ceil(result);
        }
        return  0L;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageRequest that = (PageRequest) o;
        return pageNumber == that.pageNumber && limit == that.limit;
    }

    @Override
    public int hashCode() {
        return (int) (31 + pageNumber + 31 * (31 + pageNumber) + limit);
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNumber=" + pageNumber +
                ", limit=" + limit +
                '}';
    }
}