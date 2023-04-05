# Warlock Systems

This repository is for demonstrating experience with various systems throughout
the software stack.

## Infrastructure

The [`infra`](infra/README.md) folder contains scripts for starting and managing
a Minikube cluster, as well as scripts to be shared for services to be used for
building and composing Docker images.

### Local Environment

The [`env-local`](infra/env-local/README.md) folder contains Kubernetes
manifests for deploying core components as well as apps and services whose
source code is defined in the `apps` folder.

## Apps

The `apps` folder contains source code for applications and services in
various languages to demonstrate experience with these languages and creating
interconnected systems.

### User Manager

The [User Manager](apps/user-manager/README.md) is a Java service used to manage
user accounts.
