/*
 * This file was last modified at 2025-01-16 17:39 by Victor N. Skurikhin.
 * main.go
 * $Id$
 */

package ack

import (
	"fmt"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/tool"
	"os"
)

type Factory struct {
	ModRevision int64
}

func (a Factory) Get() func() error {
	return func() error {
		f, err := os.OpenFile(revision, os.O_RDWR|os.O_CREATE|os.O_TRUNC, 0644)
		if err != nil {
			return err
		}
		defer func() { tool.FileCloseAndLog(f) }()
		_, err = fmt.Fprintf(f, "%d", a.ModRevision)
		return err
	}
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
