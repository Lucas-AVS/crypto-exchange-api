# Wallet Service API

A microservice backend for a cryptocurrency exchange platform, built with **Spring Boot**, **PostgreSQL**, and **Apache Kafka**.

This service is responsible for user management (registration and authentication) and their corresponding accounts.

## Features
- User registration and authentication (JWT-based)
- Account creation and management
- PostgreSQL persistence for users and accounts
- Kafka for asynchronous event handling
- REST endpoints documented with OpenAPI/Swagger
- Unit tests with JUnit + Mockito
- Client for communicating with other microservices (e.g., Asset Service)

## Tech Stack
- Java 21
- Spring Boot
- PostgreSQL (H2 for tests)
- Apache Kafka
- JUnit 5, Mockito
- Docker (optional)