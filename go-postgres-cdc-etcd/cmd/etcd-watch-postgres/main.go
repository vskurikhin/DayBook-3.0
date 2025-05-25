/*
 * This file was last modified at 2025-01-16 17:39 by Victor N. Skurikhin.
 * main.go
 * $Id$
 */
//!+
package main

import (
	"context"
	"crypto/tls"
	"fmt"
	"log/slog"
	"os"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/env"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/etcd_consumer"
	clientV3 "go.etcd.io/etcd/client/v3"
)

var (
	buildVersion = "N/A"
	buildDate    = "N/A"
	buildCommit  = "N/A"
)

func main() {
	run(context.Background())
}

func run(ctx context.Context) {
	yml := env.LoadYaml()
	env.SetDefaultSlog(yml.Watch.LogLevel)
	certPool := env.NewCertPool(&yml.CDC.Etcd.EtcdConfig)
	certificate := env.NewCertificate(&yml.CDC.Etcd.EtcdConfig)

	cs := fmt.Sprintf(
		"postgres://%s:%s@%s:%d/%s",
		yml.Watch.DB.Username, yml.Watch.DB.Password, yml.Watch.DB.Host, yml.Watch.DB.Port, yml.Watch.DB.Database,
	)
	pool, err := pgxpool.New(ctx, cs)
	if err != nil {
		slog.Error("new pool", "error", err)
		os.Exit(1)
	}
	messages := make(chan etcd_consumer.Message, 10000)
	go etcd_consumer.Produce(ctx, pool, messages)

	consumer := etcd_consumer.Container{
		ClientConfig: clientV3.Config{
			Endpoints: yml.Watch.Etcd.Endpoints,
			TLS: &tls.Config{
				Certificates: []tls.Certificate{certificate},
				RootCAs:      certPool,
			},
			DialTimeout: yml.Watch.Etcd.DialTimeout,
		},
		Messages: messages,
		Schema:   yml.Watch.DB.Schema,
		System:   yml.System,
		Tables:   yml.Watch.DB.Tables,
	}
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()
	consumer.Start(ctx)
}

//!-
/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
