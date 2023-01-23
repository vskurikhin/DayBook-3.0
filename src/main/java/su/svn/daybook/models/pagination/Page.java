package su.svn.daybook.models.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Page<T extends Serializable> implements Serializable {

    public static final long UPPER_BOUND = 70366596661249L;
    @Serial
    private static final long serialVersionUID = 2449524933936750999L;
    @JsonIgnore
    private final long first;
    private final long page; // page number
    private final Short rows; // page size
    private final long totalRecords;
    @JsonIgnore
    private final Long totalPages;
    private final boolean nextPage;
    private final boolean prevPage;
    private final List<T> content;
    @JsonIgnore
    private transient int hash;
    @JsonIgnore
    private transient boolean hashIsZero;

    public Page() {
        this(0, 0, null, 0, null, false, false, null);
    }

    public Page(
            long first,
            long page,
            Short rows,
            long totalRecords,
            Long totalPages,
            boolean nextPage,
            boolean prevPage,
            List<T> content) {
        this.first = first;
        this.page = page;
        this.rows = rows;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
        this.content = content;
    }

    public static <B extends Serializable> Builder<B> builder() {
        return new Builder<>();
    }

    public <L extends Serializable> Builder<L> convertToBuilderWith(List<L> list) {
        return new Builder<L>()
                .page(this.page)
                .rows(this.rows)
                .totalRecords(this.totalRecords)
                .totalPages(this.totalPages)
                .prevPage(this.prevPage)
                .nextPage(this.nextPage)
                .content(list);
    }

    public Builder<T> toBuilder() {
        return new Builder<T>()
                .page(this.page)
                .rows(this.rows)
                .totalRecords(this.totalRecords)
                .totalPages(this.totalPages)
                .content(this.content);
    }

    public long getFirst() {
        return first;
    }

    public long getPage() {
        return page;
    }

    public Short getRows() {
        return rows;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public boolean isPrevPage() {
        return prevPage;
    }

    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page<?> page = (Page<?>) o;
        return first == page.first
                && nextPage == page.nextPage
                && prevPage == page.prevPage
                && this.page == page.page
                && Objects.equals(rows, page.rows)
                && totalRecords == page.totalRecords
                && Objects.equals(totalPages, page.totalPages)
                && Objects.equals(content, page.content);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && !hashIsZero) {
            h = calculateHashCode();
            if (h == 0) {
                hashIsZero = true;
            } else {
                hash = h;
            }
        }
        return h;
    }

    private int calculateHashCode() {
        return Objects.hash(first, page, rows, totalRecords, totalPages, nextPage, prevPage, content);
    }

    @Override
    public String toString() {
        return "Page{" +
                "first=" + first +
                ", page=" + page +
                ", rows=" + rows +
                ", totalPages=" + totalPages +
                ", totalRecords=" + totalRecords +
                ", nextPage=" + nextPage +
                ", prevPage=" + prevPage +
                ", content=" + content +
                '}';
    }

    public static final class Builder<V extends Serializable> {
        private long first;
        private long page;
        private Short rows;
        private long totalRecords;
        private Long totalPages;
        private boolean nextPage;
        private boolean prevPage;
        private List<V> content;

        private Builder() {
        }

        public Builder<V> first(long first) {
            this.first = first;
            return this;
        }

        public Builder<V> page(long page) {
            this.page = page;
            return this;
        }

        public Builder<V> rows(Short rows) {
            this.rows = rows;
            return this;
        }

        public Builder<V> totalRecords(long totalElements) {
            this.totalRecords = totalElements;
            return this;
        }

        public Builder<V> totalPages(Long totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<V> nextPage(boolean nextPage) {
            this.nextPage = nextPage;
            return this;
        }

        public Builder<V> prevPage(boolean prevPage) {
            this.prevPage = prevPage;
            return this;
        }

        public Builder<V> content(List<V> content) {
            this.content = content;
            return this;
        }

        public Page<V> build() {
            return new Page<>(first, page, rows, totalRecords, totalPages, nextPage, prevPage, content);
        }
    }
}
