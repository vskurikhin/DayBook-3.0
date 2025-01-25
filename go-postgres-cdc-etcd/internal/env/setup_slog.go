/*
 * This file was last modified at 2024-08-06 18:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * yaml_config.go
 * $Id$
 */

package env

import (
	"log/slog"
	"os"
)

func SetDefaultSlog(logLevel int) {
	slog.SetDefault(slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{Level: slog.Level(logLevel)})))
}

/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
