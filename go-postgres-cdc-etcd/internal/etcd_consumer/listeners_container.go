/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * listeners_container.go
 * $Id$
 */

package etcd_consumer

import (
	"context"
	"encoding/json"
	"fmt"
	"log/slog"
	"os"
	"os/signal"
	"sync"
	"syscall"

	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/ack"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/holders"
	"go.etcd.io/etcd/api/v3/mvccpb"
	clientV3 "go.etcd.io/etcd/client/v3"
)

const EndOfUUID = "ffffffff-ffff-ffff-ffff-ffffffffffff"

var (
	cancelCh chan os.Signal
	once     sync.Once
)

type Container struct {
	ClientConfig clientV3.Config
	Messages     chan Message
	Schema       string
	System       string
	Tables       []string
}

func (l Container) Start(ctx context.Context) {
	once.Do(func() {
		for _, table := range l.Tables {
			go func(name string) {
				l.watchFor(ctx, name)
			}(table)
		}
	})
	cancelCh = make(chan os.Signal, 1)
	signal.Notify(cancelCh, syscall.SIGTERM, syscall.SIGINT, syscall.SIGABRT, syscall.SIGQUIT)
	<-cancelCh
	slog.Debug("cancel channel triggered")
}

func (l Container) watchFor(ctx context.Context, name string) {
	revision := ack.GetRevision()
	cli, errNew := clientV3.New(l.ClientConfig)

	if errNew != nil {
		slog.ErrorContext(ctx, "start clientV3.New", "error", errNew)
		os.Exit(1)
	}
	defer func() { _ = cli.Close() }()

	key := fmt.Sprintf("/%s/%s/", l.Schema, name)
	end := fmt.Sprintf("%s%s", key, EndOfUUID)
	rch := cli.Watch(context.TODO(), key, clientV3.WithPrefix(), clientV3.WithRange(end), clientV3.WithRev(revision))
	slog.Debug("watchFor", "key", key, "end", end)

	for watchResp := range rch {
		for _, ev := range watchResp.Events {
			slog.Debug("watchFor.Events", "ev", ev.Kv.Key, "Value", ev.Kv.Value)
			etcdValue, errUnmarshal := holders.Unmarshaler{EtcdKvValue: ev.Kv.Value}.Unmarshal()

			if errUnmarshal != nil {
				logUnmarshalError(errUnmarshal)
				continue
			}
			slog.Debug("watchFor.Events", "key", ev.Kv.Key, "etcdValue", etcdValue)
			if etcdValue.SystemName() == l.System {
				continue
			}
			sqlHolder, errReConstruct := etcdValue.CreateSQLHolder()

			if errReConstruct != nil {
				slog.Error("Unmarshal", "error", errReConstruct)
				continue
			}
			switch ev.Type {
			case mvccpb.PUT:
				l.Messages <- Message{
					Query: sqlHolder.UpsertSQL(),
					Args:  sqlHolder.UpsertValues(),
					Ack:   ack.Factory{ModRevision: ev.Kv.ModRevision}.Get(),
				}
			case mvccpb.DELETE:
				l.Messages <- Message{
					Query: sqlHolder.DeleteSQL(),
					Args:  sqlHolder.DeleteValues(),
					Ack:   ack.Factory{ModRevision: ev.Kv.ModRevision}.Get(),
				}
			}
		}
	}
}

func logUnmarshalError(errUnmarshal error) {
	if e, ok := errUnmarshal.(*json.SyntaxError); ok {
		slog.Error("Unmarshal", "error", errUnmarshal, "offset", e.Offset)
	} else {
		slog.Error("Unmarshal", "error", errUnmarshal, "error type", fmt.Sprintf("%T", errUnmarshal))
	}
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
