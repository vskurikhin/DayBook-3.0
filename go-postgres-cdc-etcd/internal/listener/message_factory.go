/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * message_factory.go
 * $Id$
 */

package listener

import (
	"errors"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq"
)

//goland:noinspection GoVetStructTag
type Message struct {
	ack          func() error `json:"-,omitempty"`
	Data         map[string]any
	ID           uuid.UUID
	Key          string
	MsgTime      time.Time
	OpEtcd       int
	OpPg         int
	System       string
	TblName      string
	TblNamespace string
	XLogPos      string
}

type messageFactory struct {
	Ack            func() error
	Data           map[string]any
	MessageTime    time.Time
	System         string
	TableName      string
	TableNamespace string
	XLogPos        pq.LSN
}

func (p messageFactory) createKey() (uuid.UUID, error) {
	if raw, ok1 := p.Data["id"]; ok1 {
		if i, ok2 := raw.([16]uint8); ok2 {
			a := i[0:len(i)]
			return uuid.FromBytes(a)
		}
	}
	return uuid.UUID{}, errors.New("not found id")
}

func (p messageFactory) createMessage(operationEtcd, operationPostgres int) (Message, error) {
	id, err := p.createKey()
	if err != nil {
		return Message{}, err
	}
	key := fmt.Sprintf("/%s/%s/%s", p.TableNamespace, p.TableName, id.String())
	result := Message{
		ack:          p.Ack,
		Data:         p.Data,
		ID:           id,
		Key:          key,
		MsgTime:      p.MessageTime,
		OpEtcd:       operationEtcd,
		OpPg:         operationPostgres,
		System:       p.System,
		TblName:      p.TableName,
		TblNamespace: p.TableNamespace,
		XLogPos:      p.XLogPos.String(),
	}
	if operationEtcd == PUT {
		result.Data = p.Data
	}
	return result, nil
}

func (p messageFactory) createMessageDelete() (Message, error) {
	return p.createMessage(DELETE, DELETE)
}

func (p messageFactory) createMessageInsert() (Message, error) {
	return p.createMessage(PUT, INSERT)
}

func (p messageFactory) createMessageUpdate() (Message, error) {
	return p.createMessage(PUT, UPDATE)
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
