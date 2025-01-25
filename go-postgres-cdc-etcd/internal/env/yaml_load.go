/*
 * This file was last modified at 2024-08-06 18:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * yaml_load.go
 * $Id$
 */
//!+

// Package env работа с настройками и окружением.
package env

import (
	"log/slog"
	"os"

	"github.com/spf13/viper"
)

func LoadConfig(path string) (cfg *YamlConfig, err error) {
	if os.Getenv("GO_FAVORITES_SKIP_LOAD_CONFIG") != "" {
		return &YamlConfig{}, err
	}
	viper.SetConfigName("etcd-postgres.yaml")
	viper.SetConfigType("yaml")
	viper.AddConfigPath("/etc/postgres-cdc-etcd/")
	viper.AddConfigPath(path)

	err = viper.ReadInConfig()
	if err != nil {
		return nil, err
	}
	var c YamlConfig
	err = viper.Unmarshal(&c)
	cfg = &c

	return cfg, err
}

func LoadYaml() *YamlConfig {
	yml, err := LoadConfig(".")

	if err != nil {
		slog.Error("load YAML config", "error", err)
		os.Exit(1)
	}
	return yml
}
//!-
/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */