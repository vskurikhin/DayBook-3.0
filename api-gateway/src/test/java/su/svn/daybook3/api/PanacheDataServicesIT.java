/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * DataBaseIT.java
 * $Id$
 */

package su.svn.daybook3.api;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.domain.entities.BaseRecord;
import su.svn.daybook3.api.domain.entities.JsonRecord;
import su.svn.daybook3.api.models.dto.ResourceBaseRecord;
import su.svn.daybook3.api.models.dto.ResourceJsonRecord;
import su.svn.daybook3.api.models.pagination.Page;
import su.svn.daybook3.api.models.pagination.PageRequest;
import su.svn.daybook3.api.resources.PostgresDatabaseTestResource;
import su.svn.daybook3.api.services.domain.BaseRecordDataService;
import su.svn.daybook3.api.services.domain.JsonRecordDataService;
import su.svn.daybook3.api.utils.TransactionalUniAsserterInterceptor;
import su.svn.daybook3.domain.messages.Answer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"SameParameterValue"})
@QuarkusTest
@WithTestResource(value = PostgresDatabaseTestResource.class)
public class PanacheDataServicesIT {

    @Inject
    BaseRecordDataService baseRecordDataService;

    @Inject
    JsonRecordDataService jsonRecordDataService;

    @Nested
    @DisplayName("BaseRecordDataService")
    class BaseRecordDataServiceTest {

        AtomicInteger count = new AtomicInteger(0);

        @AfterEach
        void tearDown() {
            Assertions.assertEquals(3, count.get());
        }

        @Test
        @RunOnVertxContext
        void test(UniAsserter asserter) {
            var test = ResourceBaseRecord.builder().userName("root").build();
            asserter = new TransactionalUniAsserterInterceptor(asserter);
            asserter.assertNotNull(() -> baseRecordDataService.add(test));
            asserter.assertEquals(() -> baseRecordDataService.count(), 3L);
            asserter.execute(() ->
                    baseRecordDataService.getPage(new PageRequest(0L, (short) 1))
                            .flatMap(this::doTestsWithPage)
            );
            asserter.assertEquals(() -> baseRecordDataService.count(), 2L);
        }

        private Uni<?> doTestsWithPage(Page<Answer> answerPage) {
            var o = answerPage.getContent()
                    .get(count.getAndIncrement())
                    .payload();
            if (o instanceof ResourceBaseRecord rbr) {
                var upd1 = rbr.toBuilder().flags(13).build();
                return baseRecordDataService.put(upd1)
                        .flatMap(uuid -> baseRecordDataService.get(uuid)
                                .flatMap(this::assertEqualsByUpdateAndNext)
                        );
            }
            return Uni.createFrom().voidItem();
        }

        private Uni<UUID> assertEqualsByUpdateAndNext(ResourceBaseRecord got) {
            Assertions.assertEquals(12, got.flags());
            count.getAndIncrement();
            return BaseRecord.findByBaseRecordById(got.id())
                    .flatMap(this::deleteTest);
        }

        private Uni<UUID> deleteTest(BaseRecord baseRecord) {
            baseRecord.visible(!baseRecord.visible());
            count.getAndIncrement();
            return baseRecord.update()
                    .flatMap(t -> baseRecordDataService.delete(t.id()));
        }
    }

    @Nested
    @DisplayName("JsonRecordDataService")
    class JsonRecordDataServiceTest {

        AtomicInteger count = new AtomicInteger(0);

        @AfterEach
        void tearDown() throws InterruptedException {
            Assertions.assertEquals(3, count.get());
        }

        @Test
        @RunOnVertxContext
        void test(UniAsserter asserter) {
            asserter = new TransactionalUniAsserterInterceptor(asserter);

            var test = ResourceJsonRecord.builder().userName("root").build();
            asserter.assertNotNull(() -> jsonRecordDataService.add(test));
            asserter.assertEquals(() -> jsonRecordDataService.count(), 1L);
            asserter.execute(() ->
                    jsonRecordDataService.getPage(new PageRequest(0L, (short) 1))
                            .flatMap(this::doTestsWithPage)
            );
        }

        private Uni<?> doTestsWithPage(Page<Answer> answerPage) {
            var o = answerPage.getContent()
                    .get(count.getAndIncrement())
                    .payload();
            if (o instanceof ResourceJsonRecord rbr) {
                var upd1 = rbr.toBuilder().flags(13).build();
                return jsonRecordDataService.put(upd1)
                        .flatMap(uuid -> jsonRecordDataService.get(uuid)
                                .flatMap(this::assertEqualsByUpdateAndNext)
                        );
            }
            return Uni.createFrom().voidItem();
        }

        private Uni<UUID> assertEqualsByUpdateAndNext(ResourceJsonRecord got) {
            Assertions.assertEquals(12, got.flags());
            count.getAndIncrement();
            return JsonRecord.findByJsonRecordById(got.id())
                    .flatMap(this::deleteTest);
        }

        private Uni<UUID> deleteTest(JsonRecord jsonRecord) {
            jsonRecord.visible(!jsonRecord.visible());
            count.getAndIncrement();
            return jsonRecord.update()
                    .flatMap(t -> jsonRecordDataService.delete(t.id()))
                    /*.flatMap(x -> baseRecordDataService.delete(jsonRecord.id()))*/;
        }
    }
}
