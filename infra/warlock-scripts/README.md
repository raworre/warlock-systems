# Warlock Systems Infra Scripts

This folder contains scripts that can be linked to environments for managing a
Minikube cluster and apps for building, docker image creation, and deployment.

## Running Scripts

Shared scripts can be run by calling `s/<script-name>` from an appropriately
linked Environment (`infra/env-<environment>`) or App (`apps/<app-name>`)
folder.

## App Scripts

The `app` folder contains scripts intended to be shared across apps and services
for building and creating Docker images that are then pushed to the Minikube
Docker daemon.

### Available Scripts

The following scripts are available to manage Apps:

- `s/build` runs the appropriate build commands based on project information
  found in the root project folder.

## Environment Scripts

The `environment` folder contains scripts intended to be shared across Minikube
environments to manage a given cluster.

### Available Scripts

The following scripts are available to manage Environments:

- `s/cluster-status` checks the status of the environment's Minikube cluster.
  - If the Minikube cluster is running this will return basic information
    about resources defined in the cluster.
- `s/deploy-resource` will deploy a group of resources defined by a list of
  provided `yaml` files.
- `s/destroy-cluster` will spin down all core cluster resources, stop
  the Minikube instance, and destroy the cluster completely.
  - :warning: This will completely wipe any data persisted in the MongoDB
    instance and wipe any images pushed to the Minikube Docker daemon, and 
    these resources can not be restored. :warning:
- `s/remove-resource` will remove a group of resources defined by a provided
  list of `yaml` files.
- `s/start-cluster` will start the Minikube cluster and start the basic MongoDB
  and Kong API gateway.
- `s/stop-cluster` will spin down all core cluster resources and then stop the
  Minikube instance.

