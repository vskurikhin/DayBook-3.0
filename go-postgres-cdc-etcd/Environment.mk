LD_VERSION=-X main.buildVersion=v3.10.SNAPSHOT
LD_COMMIT=-X 'main.buildDate=$$(date +'%Y-%m-%d/%H:%M:%s')' -X 'main.buildCommit=$$(git rev-parse HEAD)'
ETCD_WATCH_POSTGRES=etcd-watch-postgres
POSTGRES_CDC_ETCD=postgres-cdc-etcd