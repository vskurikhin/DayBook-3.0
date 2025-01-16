/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * message.go
 * $Id$
 */

package message

import (
	"fmt"
	"time"

	"github.com/go-playground/errors"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/message/format"
)

const (
	StreamAbortByte  Type = 'A'
	BeginByte        Type = 'B'
	CommitByte       Type = 'C'
	DeleteByte       Type = 'D'
	StreamStopByte   Type = 'E'
	InsertByte       Type = 'I'
	LogicalByte      Type = 'M'
	OriginByte       Type = 'O'
	RelationByte     Type = 'R'
	StreamStartByte  Type = 'S'
	TruncateByte     Type = 'T'
	UpdateByte       Type = 'U'
	TypeByte         Type = 'Y'
	StreamCommitByte Type = 'c'
)

const (
	XLogDataByteID                = 'w'
	PrimaryKeepaliveMessageByteID = 'k'
)

var ErrorByteNotSupported = errors.New("message byte not supported")

type Type uint8

var streamedTransaction bool

func New(data []byte, pos pq.LSN, serverTime time.Time, relation map[uint32]*format.Relation) (any, error) {
	switch Type(data[0]) {
	case InsertByte:
		return format.NewInsert(data, streamedTransaction, pos, relation, serverTime)
	case UpdateByte:
		return format.NewUpdate(data, streamedTransaction, pos, relation, serverTime)
	case DeleteByte:
		return format.NewDelete(data, streamedTransaction, pos, relation, serverTime)
	case StreamStopByte, StreamAbortByte, StreamCommitByte:
		streamedTransaction = false
		return nil, nil
	case RelationByte:
		msg, err := format.NewRelation(data, streamedTransaction)
		if err == nil {
			relation[msg.OID] = msg
		}
		return msg, err
	case StreamStartByte:
		streamedTransaction = true
		return nil, nil
	default:
		return nil, errors.Wrap(ErrorByteNotSupported, fmt.Sprintf("byte:%c, hex:0x%x ", data[0], data[0]))
	}
}
