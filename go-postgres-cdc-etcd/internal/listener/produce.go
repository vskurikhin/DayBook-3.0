/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * produce.go
 * $Id$
 */

package listener

import (
	"context"
	"encoding/json"
	"log/slog"

	clientV3 "go.etcd.io/etcd/client/v3"
)

func Produce(ctx context.Context, clientConfig clientV3.Config, messages <-chan Message) {
	cli, err := clientV3.New(clientConfig)

	if err != nil {
		slog.ErrorContext(ctx, "Produce", "error", err)
		return
	}
	for {
		select {
		case event := <-messages:
			switch event.OpEtcd {
			case DELETE:
				_, errPut := cli.Delete(ctx, event.Key)
				if errPut != nil {
					slog.Error("Delete", "error", errPut)
				}
			case PUT:
				msg, errMarshal := json.Marshal(event)
				if errMarshal != nil {
					slog.Error("Marshal", "error", errMarshal)
				}
				_, errPut := cli.Put(ctx, event.Key, string(msg))
				if errPut != nil {
					slog.Error("Put", "error", errPut)
				}
			}
			if  event.ack != nil {
				if err = event.ack(); err != nil {
					slog.Error("ack", "error", err)
				}
			}
		}
	}
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
