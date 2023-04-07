# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Added

- `environment/deploy-resource` script takes a list of Kubernetes manifest
  files and attempts to deploy them to the current Minikube cluster.
- `environment/remove-resource` script takes a list of Kubernetes manifest
  files and attempts to remove them from the current Minikube cluster.
- `app/build` script checks the current project folder for Gradle scripts or
  .NET solution files and uses the appropriate build tool to build the project.
- `app/docker-compose` script checks for a Dockerfile in the current project
  folder, extracts the app name and version from project files, and builds a
  Docker image to be tagged as `warlock-systems/<app-name>:<version>` and pushed
  to the local Minikube Docker daemon.
- `environment/dextroy-cluster` script shuts down the running Minikube cluster
  and destroys the cluster completely.
- `environment/cluster-status` script checks for the status of the Minikube
  cluster and displays information about deployed resources.
- `environment/stop-cluster` script shuts down the running Minikube cluster
- `environment/start-cluster` script checks for dependencies and starts a
  Minikube cluster with required resources to support future apps or services.
