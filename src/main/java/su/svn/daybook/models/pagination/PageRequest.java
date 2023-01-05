package su.svn.daybook.models.pagination;

import java.io.Serial;
import java.io.Serializable;

public record PageRequest(long pageNumber, short limit) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6635074121314255123L;

    public static final long UPPER_BOUND = 70366596661249L;

    public long calculateTotalPages(long totalElements) {
        var result = (double) totalElements / limit;
        if (Double.isFinite(result)) {
            return (long) Math.ceil(result);
        }
        return 0L;
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
