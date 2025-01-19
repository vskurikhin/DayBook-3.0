package ack

import (
	"log/slog"
	"os"
	"strconv"
)

const revision = "revision"

func GetRevision() int64 {
	bufRev, errReadFile := os.ReadFile(revision)

	if errReadFile != nil {
		slog.Error("start ReadFile", "error", errReadFile)
		os.Exit(1)
	}
	rev, errAtoi := strconv.ParseInt(string(bufRev), 10, 0)

	if errAtoi != nil {
		slog.Error("start ParseInt", "error", errAtoi)
		os.Exit(1)
	}
	return rev
}
