/*
 * This file was last modified at 2025-01-16 17:37 by Victor N. Skurikhin.
 * listeners_container.go
 * $Id$
 */

package listener

import (
	"log/slog"

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
	system   string
}

func (l *Container) ListenerFunc() replication.ListenerFunc {
	return func(ctx *replication.ListenerContext) {
		var err error
		var message Message

		switch msg := ctx.Message.(type) {
		case *format.Insert:
			message, err = l.newMessageFromInsert(ctx.Ack, msg)
		case *format.Delete:
			message, err = l.newMessageFromDelete(ctx.Ack, msg)
		case *format.Update:
			message, err = l.newMessageFromUpdate(ctx.Ack, msg)
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
	}
}

func (l *Container) newMessageFromDelete(ack func() error, msg *format.Delete) (Message, error) {
	return messageFactory{
		Ack:            ack,
		MessageTime:    msg.MessageTime,
		System:         l.system,
		TableName:      msg.TableName,
		TableNamespace: msg.TableNamespace,
		XLogPos:        msg.XLogPos,
	}.createMessageDelete()
}

func (l *Container) newMessageFromInsert(ack func() error, msg *format.Insert) (Message, error) {
	return messageFactory{
		Ack:            ack,
		Data:           msg.Decoded,
		MessageTime:    msg.MessageTime,
		System:         l.system,
		TableName:      msg.TableName,
		TableNamespace: msg.TableNamespace,
		XLogPos:        msg.XLogPos,
	}.createMessageInsert()
}

func (l *Container) newMessageFromUpdate(ack func() error, msg *format.Update) (Message, error) {
	return messageFactory{
		Ack:            ack,
		Data:           msg.NewDecoded,
		MessageTime:    msg.MessageTime,
		System:         l.system,
		TableName:      msg.TableName,
		TableNamespace: msg.TableNamespace,
		XLogPos:        msg.XLogPos,
	}.createMessageUpdate()
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
