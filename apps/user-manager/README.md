# User Manager

The User Manager service provides REST APIs for managing user data.
This service is built the Spring Boot framework and uses Gradle as a build tool.
A Dockerfile defines a Docker image that can be built for deploying to a
container solution.

## Prerequisites

To build and run this service, you need to have the following software installed
on your system:

* Java 11 or higher
* Gradle
* Docker

## Building the Service

A symlink to a folder named `s` should exist in the project exposing build
scripts for building the project.

To build the User Manager service, run the following command in the root
directory of the project:

```bash
s/build
```

This will compile the code, run the tests, create the service JAR file, and
push the build to local Maven for distribution.

## Building the Docker Image

To build the Docker image for the service and push it to the local Minikube
Docker daemon use the following command from the root directory of the project:

```bash
s/docker-image
```

## Running the Service

The service should be deployed to a given environment using the
`infra/env-<environment>` folder.

## API Documentation

Once the service is running, you can access the API documentation by visiting
the `/swagger-ui.html` endpoint.
