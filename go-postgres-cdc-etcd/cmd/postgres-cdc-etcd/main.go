/*
 * This file was last modified at 2025-01-16 13:34 by Victor N. Skurikhin.
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

	cdc "github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/config"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/env"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/etcd_producer"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/publication"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/slot"
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
	env.SetDefaultSlog(yml.CDC.LogLevel)
	cfg := newConfig(yml)
	certPool := env.NewCertPool(&yml.CDC.Etcd.EtcdConfig)
	certificate := env.NewCertificate(&yml.CDC.Etcd.EtcdConfig)

	clientConfig := clientV3.Config{
		Endpoints: yml.CDC.Etcd.Endpoints,
		TLS: &tls.Config{
			Certificates: []tls.Certificate{certificate},
			RootCAs:      certPool,
		},
		DialTimeout: yml.CDC.Etcd.DialTimeout,
	}
	messages := make(chan etcd_producer.Message, 1000)

	go etcd_producer.Produce(ctx, clientConfig, messages)
	lc := etcd_producer.NewListener(messages, yml.System)

	connector, errConnector := cdc.NewConnector(ctx, cfg, lc)
	if errConnector != nil {
		slog.Error("new connector", "error", errConnector)
		os.Exit(1)
	}
	connector.Start(ctx)
}

func newConfig(yml *env.YamlConfig) config.Config {
	cfg := config.Config{
		Host:     fmt.Sprintf("%s:%d", yml.CDC.DB.Host, yml.CDC.DB.Port),
		Username: yml.CDC.DB.Username,
		Password: yml.CDC.DB.Password,
		Database: yml.CDC.DB.Database,
		Publication: publication.Config{
			CreateIfNotExists: yml.CDC.DB.Publication.CreateIfNotExists,
			Name:              yml.CDC.DB.Publication.Name,
			Operations: publication.Operations{
				publication.OperationInsert,
				publication.OperationDelete,
				publication.OperationTruncate,
				publication.OperationUpdate,
			},
		},
		Slot: slot.Config{
			CreateIfNotExists:           yml.CDC.DB.Slot.CreateIfNotExists,
			Name:                        yml.CDC.DB.Slot.Name,
			SlotActivityCheckerInterval: yml.CDC.DB.Slot.ActivityCheckerInterval,
		},
		Metric: config.MetricConfig{
			Port: int(yml.CDC.MetricPort),
		},
		Logger: config.LoggerConfig{
			LogLevel: slog.Level(yml.CDC.LogLevel),
		},
	}
	return *appendPublicationTables(&cfg, yml)
}

func appendPublicationTables(cfg *config.Config, yml *env.YamlConfig) *config.Config {
	for _, name := range yml.CDC.DB.Publication.Tables {
		table := publication.Table{Name: name, ReplicaIdentity: publication.ReplicaIdentityDefault}
		cfg.Publication.Tables = append(cfg.Publication.Tables, table)
	}
	return cfg
}
//!-
/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */