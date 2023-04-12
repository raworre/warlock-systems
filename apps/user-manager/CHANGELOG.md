# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Changed

- `/login` endpoint is a POST operation and accepts a validated request body and
  now returns a JSON response with a `token` field that contains a valid JWT
  token who's `sub` field is the `username` from the login request body.
- `/login` endpoint hashes the value of the `password` field from the request
  and compares the result to the value stored in the database.

## user-manager-v0.0.1

### Added

- Exposes `/actuator/health` endpoint for general system health
- Exposes `/login` endpoint to be implemented in the future
