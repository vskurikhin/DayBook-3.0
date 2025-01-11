/*
 * This file was last modified at 2024-10-30 17:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PanachePageService.java
 * $Id$
 */

package su.svn.daybook3.api.services;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.domain.messages.Answer;
import su.svn.daybook3.utils.UniSemaphoreImpl;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static su.svn.daybook3.api.domain.entities.BaseRecord.TIMEOUT_DURATION;

@ApplicationScoped
public class PanachePageService {

    private static final Logger LOG = Logger.getLogger(PanachePageService.class);

    public <T extends PanacheEntityBase> Uni<Page<Answer>> getPage(
            @Nonnull PageRequest pageRequest,
            @Nonnull PanacheQuery<T> panacheQuery,
            @Nonnull Function<T, Answer> toAnswer) {

        LOG.tracef("getPage(%s, Supplier, BiFunction)", pageRequest);
        var mutex = new UniSemaphoreImpl(1);
        var pageQuery = panacheQuery.page((int) pageRequest.getPageNumber(), pageRequest.getLimit());

        return Uni.combine()
                .all()
                .unis(
                        mutex.protect(supplier(toAnswer, pageQuery)),
                        mutex.protect(pageQuery::count)
                )
                .asTuple()
                .onItem()
                .ifNotNull()
                .transform(t -> getAnswerPage(pageRequest, t))
                .ifNoItem()
                .after(TIMEOUT_DURATION)
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }

    private <T extends PanacheEntityBase> Supplier<Uni<List<Answer>>>
    supplier(Function<T, Answer> toAnswer, PanacheQuery<T> pageQuery) {
        return () -> pageQuery.list()
                .map(ts -> ts.stream()
                        .map(toAnswer)
                        .toList());
    }

    private Page<Answer> getAnswerPage(PageRequest pageRequest, Tuple2<List<Answer>, Long> t) {

        var content = t.getItem1();
        long totalElements = t.getItem2();
        var totalPages = (long) Math.ceil((double) totalElements / pageRequest.getLimit());
        var currentPageSize = (short) (content.size()); // TODO check&log
        var limitByOffset = pageRequest.getLimit() * pageRequest.getPageNumber(); // TODO check&log
        var currentCountShowElements = limitByOffset + currentPageSize;
        LOG.tracef(
                "totalElements: %d, totalPages: %d, currentPageSize: %d, limitByOffset: %d, currentCountShowElements: %d",
                totalElements, totalPages, currentPageSize, limitByOffset, currentCountShowElements
        );
        var hasPrevPage = pageRequest.getPageNumber() > 0 && totalElements > 0;
        var hasNextPage = currentCountShowElements < totalElements;

        return Page.<Answer>builder()
                .content(content)
                .totalRecords(totalElements)
                .totalPages(totalPages)
                .page(pageRequest.getPageNumber())
                .rows(currentPageSize)
                .nextPage(hasNextPage)
                .prevPage(hasPrevPage)
                .build();
    }
}
