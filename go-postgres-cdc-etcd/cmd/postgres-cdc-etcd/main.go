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
	"crypto/x509"
	"fmt"
	"log/slog"
	"os"

	cdc "github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/config"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/env"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/internal/listener"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/publication"
	"github.com/vskurikhin/DayBook-3.10/go-postgres-cdc-etcd/pq/slot"
	clientV3 "go.etcd.io/etcd/client/v3"
)

func main() {
	run(context.Background())
}

func run(ctx context.Context) {
	yml := loadYaml()
	setDefaultSlog(yml.CDC.LogLevel)
	cfg := newConfig(yml)
	certPool := newCertPool(yml)
	certificate := newCertificate(yml)

	clientConfig := clientV3.Config{
		Endpoints: yml.CDC.Etcd.Endpoints,
		TLS: &tls.Config{
			Certificates: []tls.Certificate{certificate},
			RootCAs:      certPool,
		},
		DialTimeout: yml.CDC.Etcd.DialTimeout,
	}
	messages := make(chan listener.Message, 1000)

	go listener.Produce(ctx, clientConfig, messages)
	lc := listener.New(messages, yml.CDC.System)

	connector, errConnector := cdc.NewConnector(ctx, cfg, lc)
	if errConnector != nil {
		slog.Error("new connector", "error", errConnector)
		os.Exit(1)
	}
	connector.Start(ctx)
}

func setDefaultSlog(logLevel int) {
	slog.SetDefault(slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{Level: slog.Level(logLevel)})))
}

func newCertificate(yml *env.YamlConfig) tls.Certificate {
	cer, err := tls.LoadX509KeyPair(yml.CDC.Etcd.CertFile, yml.CDC.Etcd.KeyFile)
	if err != nil {
		slog.Error("load CertFile and KeyFile", "error", err)
		os.Exit(1)
	}
	return cer
}

func newCertPool(yml *env.YamlConfig) *x509.CertPool {
	certPool, err := x509.SystemCertPool()
	if err != nil {
		slog.Error("system CertPool", "error", err)
		os.Exit(1)
	}
	for _, file := range yml.CDC.Etcd.CACertFiles {
		if caCertPEM, err := os.ReadFile(file); err != nil {
			slog.Error("read CA Cert", "error", err, "file", file)
			os.Exit(1)
		} else if ok := certPool.AppendCertsFromPEM(caCertPEM); !ok {
			slog.Error("append CA Cert", "error", err, "file", file)
			os.Exit(1)
		}
	}
	return certPool
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

func loadYaml() *env.YamlConfig {
	yml, err := env.LoadConfig(".")
	if err != nil {
		slog.Error("load YAML config", "error", err)
		os.Exit(1)
	}
	return yml
}
//!-
/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */