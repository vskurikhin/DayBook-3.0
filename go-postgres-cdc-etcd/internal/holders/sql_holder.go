/*
 * This file was last modified at 2025-01-16 17:39 by Victor N. Skurikhin.
 * main.go
 * $Id$
 */

package holders

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"strconv"
	"strings"
	"time"

	"github.com/google/uuid"
)

var (
	UpsertQuery = "INSERT INTO %s (%s) VALUES (%s) ON CONFLICT (id) DO UPDATE SET %s;"
	DeleteQuery = "DELETE FROM %s WHERE id = $1;"
)

type SQLHolder interface {
	DeleteSQL() string
	DeleteValues() []any
	UpsertSQL() string
	UpsertValues() []any
}

type SQLHolderFactory interface {
	CreateSQLHolder() (SQLHolder, error)
	SystemName() string
}

type Unmarshaler struct {
	EtcdKvValue []byte
	system      string
}

func (d Unmarshaler) Unmarshal() (SQLHolderFactory, error) {
	message := valueMessage{}
	errUnmarshal := json.Unmarshal(d.EtcdKvValue, &message)

	if errUnmarshal != nil {
		return nil, errUnmarshal
	}
	return &message, nil
}

type valueMessage struct {
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

func (u valueMessage) CreateSQLHolder() (SQLHolder, error) {
	if len(u.Data) < 1 {
		return nil, errors.New("not ok #1")
	}
	id, ok := u.Data["id"]
	if !ok {
		return nil, errors.New("not ok #2")
	}
	columns := make([]string, 0, len(u.Data))
	columns = append(columns, "id")
	values := make([]any, 0, len(u.Data))
	values = appendId(values, id, u.ID)
	excluded := make([]string, 0, len(u.Data))

	for key, value := range u.Data {
		if key == "id" || key == "create_time" || key == "update_time" {
			continue
		}
		if key == "parent_id" {
			switch a := value.(type) {
			case []interface{}:
				parentID, err := convertFromFloat64(a)
				if err != nil {
					slog.Error("CreateSQLHolder", "error", err)
					continue
				}
				value = parentID
			default:
				continue
			}
		}
		if key == "local_change" {
			switch value.(type) {
			case bool:
				value = true
			case int32:
				value = 0
			case int:
				value = 0
			case int64:
				value = 0
			case float32:
				value = 0
			case float64:
				value = 0
			}
		}
		if key == "flags" {
			switch i := value.(type) {
			case int32:
				value = i | 1
			case int:
				value = i | 1
			case int64:
				value = i | 1
			case float32:
				value = int(i) | 1
			case float64:
				value = int(i) | 1
			}
		}
		columns = append(columns, key)
		values = append(values, value)
		excluded = append(excluded, fmt.Sprintf("%s = excluded.%s", key, key))
	}
	return &sqlHolder{
		columns:        columns,
		excluded:       excluded,
		tableName:      u.TblName,
		tableNamespace: u.TblNamespace,
		values:         values,
	}, nil
}

func (u valueMessage) SystemName() string {
	return u.System
}

type sqlHolder struct {
	columns        []string
	excluded       []string
	tableName      string
	tableNamespace string
	values         []any
}

func (s sqlHolder) DeleteSQL() string {
	tableFullName := fmt.Sprintf("%s.%s", s.tableNamespace, s.tableName)

	return fmt.Sprintf(DeleteQuery, tableFullName)
}

func (s sqlHolder) UpsertSQL() string {
	tableFullName := fmt.Sprintf("%s.%s", s.tableNamespace, s.tableName)
	columns := strings.Join(s.columns, ", ")
	var bufPh bytes.Buffer
	bufPh.WriteString("$1")
	for i := 1; i < len(s.columns); i++ {
		bufPh.WriteString(", $")
		bufPh.WriteString(strconv.Itoa(i + 1))
	}
	excluded := strings.Join(s.excluded, ", ")

	return fmt.Sprintf(UpsertQuery, tableFullName, columns, bufPh.String(), excluded)
}

func (s sqlHolder) DeleteValues() []any {
	return []any{s.values[0]}
}

func (s sqlHolder) UpsertValues() []any {
	return s.values
}

func appendId(values []any, unknownID any, uuidID uuid.UUID) []any {
	var id uuid.UUID
	var errConvert error
	if su, ok1 := unknownID.([]any); ok1 && len(su) == 16 {
		switch su[0].(type) {
		case float64:
			id, errConvert = convertFromFloat64(su)
		default:
			errConvert = errors.New("unknown type")
		}
	} else if errConvert != nil {
		id = uuidID
	}
	values = append(values, id)
	return values
}

func convertFromFloat64(su []any) (uuid.UUID, error) {
	if _, ok := su[0].(float64); ok {
		bytez := make([]byte, 16)
		for i := 0; i < len(su); i++ {
			bytez[i] = byte(su[i].(float64))
		}
		return uuid.FromBytes(bytez)
	}
	if _, ok := su[0].(*float64); ok {
		bytez := make([]byte, 16)
		for i := 0; i < len(su); i++ {
			bytez[i] = byte(*(su[i].(*float64)))
		}
		return uuid.FromBytes(bytez)
	}
	return [16]byte{}, errors.New("unknown type")
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
