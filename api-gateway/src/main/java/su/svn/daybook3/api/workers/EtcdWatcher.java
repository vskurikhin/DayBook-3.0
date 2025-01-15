/*
 * This file was last modified at 2025-01-15 16:48 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * EtcdWatcher.java
 * $Id$
 */

package su.svn.daybook3.api.workers;

import com.google.protobuf.ByteString;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;
import ro.gs1.quarkus.etcd.api.EtcdClient;
import ro.gs1.quarkus.etcd.api.EtcdClientChannel;
import ro.gs1.quarkus.etcd.api.WatchCreateRequest;
import ro.gs1.quarkus.etcd.api.WatchRequest;
import ro.gs1.quarkus.etcd.api.WatchResponse;
import ro.gs1.quarkus.etcd.api.kv.Event;
import su.svn.daybook3.api.services.cache.JsonRecordCacheProvider;
import su.svn.daybook3.domain.messages.Answer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

@Singleton
public class EtcdWatcher implements Runnable {

    private static final Logger LOG = Logger.getLogger(EtcdWatcher.class);

    private static final String UUID_BEGIN = new UUID(0, 0).toString();
    private static final String UUID_END = "ffffffff-ffff-ffff-ffff-ffffffffffff";
    private static final String DB_JSON_RECORD = "/db/json_records/";
    private static final ByteString KEY = ByteString.copyFrom(DB_JSON_RECORD + UUID_BEGIN, StandardCharsets.UTF_8);
    private static final ByteString RANGE_END = ByteString.copyFrom(DB_JSON_RECORD + UUID_END, StandardCharsets.UTF_8);
    private static final Pattern pattern = Pattern.compile(DB_JSON_RECORD + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})");

    @EtcdClient("client1")
    EtcdClientChannel client;

    @Inject
    JsonRecordCacheProvider jsonRecordCacheProvider;

    @Startup
    void init() {
        new Thread(this).start();
    }

    private void accept(Event event) {
        var key = event.getKv().getKey().toStringUtf8();
        LOG.tracef("key: %s", key);
        var matcher = pattern.matcher(key);

        if (matcher.find()) {
            LOG.debugf("groupCount: %d, group#1: %s)", matcher.groupCount(), matcher.group(1));
            jsonRecordCacheProvider.invalidate(Answer.empty())
                    .await()
                    .indefinitely();
            if (matcher.groupCount() > 0) {
                jsonRecordCacheProvider.invalidateByKey(UUID.fromString(matcher.group(1)), Answer.empty())
                        .await()
                        .indefinitely();
            }
        }
    }

    private void action(WatchResponse watchResponse) {
        watchResponse.getEventsList()
                .forEach(this::accept);
    }

    @SneakyThrows
    @Override
    public void run() {
        LOG.infof("run: Thread: %s", Thread.currentThread().toString());
        LOG.debugf("run: Working Directory: %s", System.getProperty("user.dir"));
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                var watchCreateRequest = WatchCreateRequest.newBuilder()
                        .setKey(KEY)
                        .setRangeEnd(RANGE_END)
                        .build();
                var watchRequest = WatchRequest.newBuilder()
                        .setCreateRequest(watchCreateRequest)
                        .build();
                client.getWatchClient()
                        .watch(Multi.createFrom().item(watchRequest))
                        .subscribe()
                        .asStream()
                        .forEach(this::action);
            } catch (Throwable t) {
                LOG.errorf("run: %s", t.getMessage());
                if (LOG.isTraceEnabled()) {
                    StringWriter stackTrace = new StringWriter();
                    t.printStackTrace(new PrintWriter(stackTrace));
                    LOG.tracef("run: ERROR: %s", stackTrace.toString());
                }
                //noinspection BusyWait
                Thread.sleep(500);
            }
        }
    }
}
