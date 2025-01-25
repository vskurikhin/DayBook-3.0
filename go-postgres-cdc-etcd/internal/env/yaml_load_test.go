/*
 * This file was last modified at 2024-07-10 20:32 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * yaml_load_test.go
 * $Id$
 */
//!+

// Package env работа с настройками и окружением.
package env

import (
	"fmt"
	"os"
	"testing"
	"time"

	"github.com/spf13/viper"
	"github.com/stretchr/testify/assert"
)

type wantLoadConfig struct {
	yamlConfig *YamlConfig
	err        error
}

type testLoadConfig struct {
	name  string
	input string
	fRun  func(string) (*YamlConfig, error)
	want  wantLoadConfig
}

func TestLoadConfig(t *testing.T) {
	assert.NotNil(t, t)
	var tests = []testLoadConfig{
		{
			name:  `test #1 positive LoadConfig(".")`,
			input: ".",
			fRun:  LoadConfig,
			want: wantLoadConfig{
				yamlConfig: &YamlConfig{
					CDC: struct {
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
					}{
						DB: struct {
							DBConfig    `mapstructure:",squash"`
							Publication struct {
								PublicationConfig `mapstructure:",squash"`
							}
							Schema string
							Slot   struct {
								SlotConfig `mapstructure:",squash"`
							}
						}{
							DBConfig: DBConfig{
								Database: "db",
								Host:     "localhost",
								Port:     5432,
								Username: "replicator",
								Password: "replicator_password",
							},
							Publication: struct {
								PublicationConfig `mapstructure:",squash"`
							}{
								PublicationConfig: PublicationConfig{
									CreateIfNotExists: true,
									Name:              "pub_db",
									Tables:            []string{"table1", "table2"},
								},
							},
							Schema: "db",
							Slot: struct {
								SlotConfig `mapstructure:",squash"`
							}{
								SlotConfig: SlotConfig{
									CreateIfNotExists:       true,
									Name:                    "cdc_slot",
									ActivityCheckerInterval: 3000 * time.Nanosecond,
								},
							},
						},
						Etcd: struct {
							EtcdConfig `mapstructure:",squash"`
						}{
							EtcdConfig: EtcdConfig{
								CACertFiles: []string{"ca.crt", "intermediate.crt"},
								CertFile:    "test1.crt",
								DialTimeout: 2 * time.Second,
								Endpoints:   []string{"localhost:1379", "localhost:2379", "localhost:3379"},
								KeyFile:     "test1.key",
							},
						},
						MetricPort: 65535,
						LogLevel:   -4,
					},
					System: "test",
					Watch: struct {
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
					}{
						DB: struct {
							DBConfig `mapstructure:",squash"`
							Tables   []string
							Schema   string
						}{
							DBConfig: DBConfig{
								Database: "db",
								Host:     "localhost",
								Port:     5432,
								Username: "dbuser",
								Password: "password",
							},
							Tables: []string{"tbl1", "tbl2", "tbl3"},
							Schema: "db",
						},
						Etcd: struct {
							EtcdConfig `mapstructure:",squash"`
						}{
							EtcdConfig: EtcdConfig{
								CACertFiles: []string{"intermediate.crt", "ca.crt"},
								CertFile:    "test2.crt",
								DialTimeout: 2 * time.Second,
								Endpoints:   []string{"localhost:3379", "localhost:2379", "localhost:1379"},
								KeyFile:     "test2.key",
							},
						},
						MetricPort: 65535,
						LogLevel:   -4,
					},
				},
				err: nil,
			},
		},
		{
			name:  `test #2 positive GO_FAVORITES_SKIP_LOAD_CONFIG=True LoadConfig("")`,
			input: "test",
			fRun: func(s string) (*YamlConfig, error) {
				t.Setenv("GO_FAVORITES_SKIP_LOAD_CONFIG", "True")
				return LoadConfig(s)
			},
			want: wantLoadConfig{
				yamlConfig: &YamlConfig{},
				err:        nil,
			},
		},
	}
	assert.NotNil(t, t)
	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			got, err := test.fRun(test.input)
			assert.Equal(t, test.want.yamlConfig, got)
			assert.Equal(t, test.want.err, err)
		})
	}
}

func configFileNotFoundError(path string) error {
	wd := chDir()
	defer func() { _ = os.Chdir(wd) }()

	viper.SetConfigName("etcd-postgres.yaml")
	viper.SetConfigType("yaml")
	viper.AddConfigPath("/etc/etcd-proxy/")
	viper.AddConfigPath(path)

	return viper.ReadInConfig()
}

func chDir() string {
	wd, _ := os.Getwd()
	if err := os.Chdir("test"); err != nil {
		fmt.Println("Chdir error:", err)
	}
	return wd
}

/*
Cannot use 'struct { DB struct { DBConfig `mapstructure:",squash"` Publication struct{ PublicationConfig `mapstructure:",squash"` } Slot struct { SlotConfig `mapstructure:",squash"` } } Etcd struct { EtcdConfig `mapstructure:",squash"` } }{ DB: struct { DBConfig `mapstructure:",squash"` Publication struct{ PublicationConfig `mapstructure:",squash"` } Slot struct { SlotConfig `mapstructure:",squash"` } }{ DBConfig: DBConfig{ Database: "db", Host: "localhost", Port: 5432, Username: "dbuser", Password: "password", }, Publication: struct { PublicationConfig `mapstructure:",squash"` }{ PublicationConfig: PublicationConfig{ CreateIfNotExists: true, Name: "pub_db", Tables: []string{"json_records"}, }, }, Slot: struct { SlotConfig `mapstructure:",squash"` }{ SlotConfig: SlotConfig{ CreateIfNotExists: true, Name: "pub_db", ActivityCheckerInterval: 0, }, }, }, Etcd: struct { EtcdConfig `mapstructure:",squash"` }{ EtcdConfig: EtcdConfig{ CACertFiles: []string{"ca.crt", "intermediate.crt"}, CertFile: "test.crt", DialTimeout: 2 * time.Second, Endpoints: []string{"localhost:1379", "localhost:2379", "localhost:3379"}, KeyFile: "test.key", }, }, }' (type struct {...}) as the type struct {...}
*/
