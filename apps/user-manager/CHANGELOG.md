# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

## user-manager-v0.0.2

### Added

- `/register` endpoint checks that registration request is valid, if username
  already exists in database, and, if not, creates a new user and profile and
  returns a token and an `Authorization` header.
- `/profile` endpoint reads the `Bearer` token from the `Authorization` header.

### Changed

- `/login` endpoint is a POST operation and accepts a validated request body and
  now returns a JSON response with a `token` field that contains a valid JWT
  token who's `sub` field is the `username` from the login request body.
- `/login` endpoint hashes the value of the `password` field from the request
  and compares the result to the value stored in the database.
- `/login` endpoint now adds the `token` from the response body to the 
  `Authorization` header of the response.

## user-manager-v0.0.1 2023-04-09

### Added

- Exposes `/actuator/health` endpoint for general system health
- Exposes `/login` endpoint to be implemented in the future
