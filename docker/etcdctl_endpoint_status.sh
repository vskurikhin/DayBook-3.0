#!/bin/sh
etcdctl \
	--cacert ./etcdata/etcd-tls/isrgrootx1.pem \
	--key./etcdata/etcd-tls/tls.key \
	--cert ./etcdata/etcd-tls/tls.crt \
	--endpoints=localhost.localdomain:2379,localhost.localdomain:22379,localhost.localdomain:32379 \
	--write-out=table endpoint status
