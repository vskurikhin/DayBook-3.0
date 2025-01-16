/*
 * Copyright text:
 * This file was last modified at 2024-07-10 20:02 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * yaml_config_test.go
 * $Id$
 */
//!+

// Package env работа с настройками и окружением.
package env

import (
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestYamlConfig(t *testing.T) {
	var tests = []struct {
		name  string
		fRun  func() *YamlConfig
		isNil bool
		want  string
	}{
		{
			name:  `positive test #0 nil yamlConfig`,
			fRun:  nilYamlConfig,
			isNil: true,
			want: `<nil>`,
		},
		{
			name: `positive test #1 zero yamlConfig`,
			fRun: zeroYamlConfig,
			want: `&{{{{  0   {{false  []}} {{false  0s}}}} {{[]  0s [] }}}}`,
		},
	}
	assert.NotNil(t, t)
	for _, test := range tests {
		t.Run(test.name, func(t *testing.T) {
			got := test.fRun()
			if !test.isNil {
				assert.Equal(t, test.want, fmt.Sprintf("%v", got))
			} else {
				assert.Equal(t, test.want, fmt.Sprintf("%v", (*YamlConfig)(nil)))
			}
		})
	}
}

func nilYamlConfig() *YamlConfig {
	return nil
}

func zeroYamlConfig() *YamlConfig {
	return &YamlConfig{}
}

//!-
/* vim: set tabstop=4 softtabstop=4 shiftwidth=4 noexpandtab: */