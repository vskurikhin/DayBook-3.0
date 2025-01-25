/*
 * This file was last modified at 2024-08-06 18:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * yaml_config.go
 * $Id$
 */
//!+

// Package env работа с настройками и окружением.
package env

import (
	"crypto/tls"
	"crypto/x509"
	"log/slog"
	"os"
	"time"
)

type YamlConfig struct {
	CDC struct {
		DB struct {
			DBConfig    `mapstructure:",squash"`
			Publication struct {
				PublicationConfig `mapstructure:",squash"`
			}
			Schema string
			Slot   struct {
				SlotConfig `mapstructure:",squash"`
			}
		}
		Etcd struct {
			EtcdConfig `mapstructure:",squash"`
		}
		MetricPort uint16 `mapstructure:"metric_port"`
		LogLevel   int    `mapstructure:"log_level"`
	}
	System string
	Watch  struct {
		DB struct {
			DBConfig `mapstructure:",squash"`
			Tables   []string
			Schema   string
		}
		Etcd struct {
			EtcdConfig `mapstructure:",squash"`
		}
		MetricPort uint16 `mapstructure:"metric_port"`
		LogLevel   int    `mapstructure:"log_level"`
	}
}

type DBConfig struct {
	Database string
	Host     string
	Port     int16
	Username string
	Password string `mapstructure:"password"`
}

type EtcdConfig struct {
	CACertFiles []string      `mapstructure:"ca_certs"`
	CertFile    string        `mapstructure:"cert_file"`
	DialTimeout time.Duration `mapstructure:"dial_timeout"`
	Endpoints   []string      `mapstructure:"endpoints"`
	KeyFile     string        `mapstructure:"key_file"`
}

type PublicationConfig struct {
	CreateIfNotExists bool `mapstructure:"create_if_not_exists"`
	Name              string
	Tables            []string
}

type SlotConfig struct {
	CreateIfNotExists       bool          `mapstructure:"create_if_not_exists"`
	Name                    string        `mapstructure:"name"`
	ActivityCheckerInterval time.Duration `mapstructure:"activity_checker_interval"`
}

func NewCertificate(ecfg *EtcdConfig) tls.Certificate {
	cer, err := tls.LoadX509KeyPair(ecfg.CertFile, ecfg.KeyFile)
	if err != nil {
		slog.Error("load CertFile and KeyFile", "error", err)
		os.Exit(1)
	}
	return cer
}

func NewCertPool(ecfg *EtcdConfig) *x509.CertPool {
	certPool, err := x509.SystemCertPool()
	if err != nil {
		slog.Error("system CertPool", "error", err)
		os.Exit(1)
	}
	for _, file := range ecfg.CACertFiles {
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
//!-
/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */
