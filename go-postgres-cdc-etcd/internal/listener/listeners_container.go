/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * listeners_container.go
 * $Id$
 */

package listener

import (
	"log/slog"
	"sync/atomic"

	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/message/format"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/replication"
)

const (
	_ = iota
	INSERT
	UPDATE
	DELETE
	TRUNCATE
	PUT
)

var _ replication.Listeners = (*Container)(nil)

func New(messages chan Message, system string) *Container {
	return &Container{messages: messages, system: system}
}

type Container struct {
	messages chan Message
	xLogPos  atomic.Uint64
	system   string
}

func (l *Container) ListenerFunc() replication.ListenerFunc {
	return func(ctx *replication.ListenerContext) {
		var err error
		var message Message
		xLogPos := l.xLogPos.Load()

		switch msg := ctx.Message.(type) {
		case *format.Insert:
			message, err = l.newMessageFromInsert(ctx.Ack, xLogPos, msg)
		case *format.Delete:
			message, err = l.newMessageFromDelete(ctx.Ack, xLogPos, msg)
		case *format.Update:
			message, err = l.newMessageFromUpdate(ctx.Ack, xLogPos, msg)
		}
		if err != nil {
			slog.Error("ListenerFunc", "error", err)
		} else {
			l.messages <- message
		}
	}
}

func (l *Container) SendLSNHookFunc() replication.SendLSNHookFunc {
	return func(xLogPos pq.LSN) {
		slog.Debug("send stand by status update", "xLogPos", xLogPos.String())
	}
}

func (l *Container) SinkHookFunc() replication.SinkHookFunc {
	return func(xLogData *replication.XLogData) {
		slog.Debug("SinkHookFunc", "WALStart", xLogData.WALStart.String(), "WALEnd", xLogData.ServerWALEnd.String())
		l.xLogPos.Store(uint64(xLogData.ServerWALEnd))
	}
}

func (l *Container) newMessageFromDelete(ack func() error, pos uint64, msg *format.Delete) (Message, error) {
	return messageFactory{
		Ack:            ack,
		MessageTime:    msg.MessageTime,
		System:         l.system,
		TableName:      msg.TableName,
		TableNamespace: msg.TableNamespace,
		XLogPos:        pq.LSN(pos),
	}.createMessageDelete()
}

func (l *Container) newMessageFromInsert(ack func() error, pos uint64, msg *format.Insert) (Message, error) {
	return messageFactory{
		Ack:            ack,
		Data:           msg.Decoded,
		MessageTime:    msg.MessageTime,
		System:         l.system,
		TableName:      msg.TableName,
		TableNamespace: msg.TableNamespace,
		XLogPos:        pq.LSN(pos),
	}.createMessageInsert()
}

func (l *Container) newMessageFromUpdate(ack func() error, pos uint64, msg *format.Update) (Message, error) {
	return messageFactory{
		Ack:            ack,
		Data:           msg.NewDecoded,
		MessageTime:    msg.MessageTime,
		System:         l.system,
		TableName:      msg.TableName,
		TableNamespace: msg.TableNamespace,
		XLogPos:        pq.LSN(pos),
	}.createMessageUpdate()
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
