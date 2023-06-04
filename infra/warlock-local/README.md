# Warlock Local Environment

This folder contains links to shared scripts and Kubernetes manifests for
managing the Warlock Local Environment.

## Prerequisites

To run this local development environment the following must be installed on the
host system:

- Minikube
- Kubectl
- Docker
- Symlink in project folder named `s` that links to `infra/scripts/environment`

## Running the Environment

Use the following command to start the `env-local` Minikube cluster and deploy
MongoDB and Kong API gateway resources to the cluster.

```bash
s/start-cluster
```
