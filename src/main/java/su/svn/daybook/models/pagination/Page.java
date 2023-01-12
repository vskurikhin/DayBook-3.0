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
    private final long pageNumber;
    private final Short pageSize;
    private final long totalElements;
    private final Long totalPages;
    private final boolean nextPage;
    private final boolean prevPage;
    private final List<T> content;
    @JsonIgnore
    private transient int hash;
    @JsonIgnore
    private transient boolean hashIsZero;

    public Page() {
        this(0, null, 0, null, false, false, null);
    }

    public Page(
            long pageNumber,
            Short pageSize,
            long totalElements,
            Long totalPages,
            boolean nextPage,
            boolean prevPage,
            List<T> content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
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
                .pageNumber(this.pageNumber)
                .pageSize(this.pageSize)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .prevPage(this.prevPage)
                .nextPage(this.nextPage)
                .content(list);
    }

    public Builder<T> toBuilder() {
        return new Builder<T>()
                .pageNumber(this.pageNumber)
                .pageSize(this.pageSize)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .content(this.content);
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public Short getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
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
        return nextPage == page.nextPage
                && prevPage == page.prevPage
                && pageNumber == page.pageNumber
                && Objects.equals(pageSize, page.pageSize)
                && totalElements == page.totalElements
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
        return Objects.hash(pageNumber, pageSize, totalElements, totalPages, nextPage, prevPage, content);
    }

    @Override
    public String toString() {
        return "Page{" +
                "totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageNumber +
                ", nextPage=" + nextPage +
                ", prevPage=" + prevPage +
                ", content=" + content +
                '}';
    }

    public static final class Builder<V extends Serializable> {
        private long pageNumber;
        private Short pageSize;
        private long totalElements;
        private Long totalPages;
        private boolean nextPage;
        private boolean prevPage;
        private List<V> content;

        private Builder() {
        }

        public Builder<V> pageNumber(long pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder<V> pageSize(Short pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder<V> totalElements(long totalElements) {
            this.totalElements = totalElements;
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
            return new Page<>(pageNumber, pageSize, totalElements, totalPages, nextPage, prevPage, content);
        }
    }
}
