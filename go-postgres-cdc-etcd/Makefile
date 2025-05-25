include Environment.mk

PROJECTNAME=$(shell basename "$(PWD)")

default: build

.PHONY: init
init: init/lint

.PHONY: init/lint  init/vulnCheck
init/lint:
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@v1.59.1
	go install golang.org/x/tools/go/analysis/passes/fieldalignment/cmd/fieldalignment@v0.22.0

.PHONY: init/vulnCheck
init/vulnCheck:
	go install golang.org/x/vuln/cmd/govulncheck@latest

.PHONY: audit
audit: vendor
	@echo 'Formatting code...'
	fieldalignment -fix ./...
	golangci-lint run -c .golangci.yml --timeout=5m -v --fix
	@echo 'Vetting code...'
	go vet ./...
	@echo 'Vulnerability scanning...'
	govulncheck ./...

.PHONY: tidy
tidy:
	@echo 'Tidying and verifying module dependencies...'
	go mod tidy -compat=1.22.4
	go mod verify

.PHONY: tidy/all
tidy/all:
	go mod tidy
	cd integration_test && go mod tidy && cd ../

.PHONY: test/integration
test/integration:
	cd integration_test && go test -race -p=1 -v ./...

.PHONY: lint
lint: init/lint
	@echo 'Formatting code...'
	fieldalignment -fix ./...
	golangci-lint run -c .golangci.yml --timeout=5m -v --fix

go-clean:
	@echo "  >  Cleaning build cache"
	@GOPATH=$(GOPATH) GOBIN=$(GOBIN) go clean

## clean: Clean build files. Runs `go clean` internally.
clean:
	@(MAKEFILE) go-clean

## build: Compile the binaries.
build: build/$(ETCD_WATCH_POSTGRES) build/$(POSTGRES_CDC_ETCD)

## build/etcd-watch-postgres: Build and the binary compile etcd-watch-postgres
build/$(ETCD_WATCH_POSTGRES):
	@echo "  >  Building $(ETCD_WATCH_POSTGRES) binary..."
	@GOPATH=$(GOPATH) GOBIN=$(GOBIN) cd ./cmd/$(ETCD_WATCH_POSTGRES) && go build -ldflags "$(LD_VERSION) $(LD_COMMIT)" -o ../../bin/$(ETCD_WATCH_POSTGRES) $(GOFILES)

## build/etcd-watch-postgres: Build and the binary compile postgres-cdc-etcd
build/$(POSTGRES_CDC_ETCD):
	@echo "  >  Building $(POSTGRES_CDC_ETCD) binary..."
	@GOPATH=$(GOPATH) GOBIN=$(GOBIN) cd ./cmd/$(POSTGRES_CDC_ETCD) && go build -ldflags "$(LD_VERSION) $(LD_COMMIT)" -o ../../bin/$(POSTGRES_CDC_ETCD) $(GOFILES)

.PHONY: help
all: help
help: Makefile
	@echo
	@echo " Choose a command run in "$(PROJECTNAME)":"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo
