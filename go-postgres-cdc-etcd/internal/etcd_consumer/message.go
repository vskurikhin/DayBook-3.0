/*
 * This file was last modified at 2025-01-16 13:36 by Victor N. Skurikhin.
 * message.go
 * $Id$
 */

package etcd_consumer


type Message struct {
	Query string
	Args  []any
	Ack         func() error
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
