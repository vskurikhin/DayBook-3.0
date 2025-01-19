/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * produce.go
 * $Id$
 */

package etcd_consumer

import (
	"context"
	"errors"
	"log/slog"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

func Produce(ctx context.Context, w *pgxpool.Pool, messages <-chan Message) {
	var lastAck func() error
	counter := 0
	bulkSize := 10000

	queue := make([]*pgx.QueuedQuery, bulkSize)

	for {
		select {
		case event := <-messages:
			lastAck = event.Ack

			queue[counter] = &pgx.QueuedQuery{SQL: event.Query, Arguments: event.Args}
			counter++
			if counter == bulkSize {
				batchResults := w.SendBatch(ctx, &pgx.Batch{QueuedQueries: queue})
				err := Exec(batchResults, counter)
				if err != nil {
					slog.Error("batch results", "error", err)
					continue
				}
				slog.Info("postgresql write", "count", counter)
				counter = 0
				if err = event.Ack(); err != nil {
					slog.Error("ack", "error", err)
				}
			}

		case <-time.After(time.Millisecond):
			if counter > 0 {
				batchResults := w.SendBatch(ctx, &pgx.Batch{QueuedQueries: queue[:counter]})
				err := Exec(batchResults, counter)
				if err != nil {
					slog.Error("batch results", "error", err)
					continue
				}
				slog.Info("postgresql write", "count", counter)
				counter = 0
				if err = lastAck(); err != nil {
					slog.Error("ack", "error", err)
				}
			}
		}
	}
}

func Exec(br pgx.BatchResults, sqlCount int) error {
	defer func() { _ = br.Close() }()
	var batchErr error
	for t := 0; t < sqlCount; t++ {
		_, err := br.Exec()
		if err != nil {
			batchErr = errors.Join(batchErr, err)
		}
	}
	return batchErr
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
