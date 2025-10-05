
# Voyager People Management

A small Spring Boot application for managing cargos, departamentos and funcionarios (Module 1).

## Requirements

- Java 21
- Maven (the included ./mvnw wrapper can be used)
- Docker (optional, for local PostgreSQL)

## Running locally with Docker (recommended)

1. Start PostgreSQL with docker-compose (this creates a database `voyager` and user `voyager` / password `voyager`):

	docker-compose up -d

2. Run the application:

	./mvnw spring-boot:run

The application reads the JDBC configuration from environment variables with sensible defaults. The defaults are:

- JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/voyager
- JDBC_DATABASE_USERNAME=voyager
- JDBC_DATABASE_PASSWORD=voyager

If you prefer a different database or credentials, set the environment variables before running the app.

## Running without Docker

Make sure you have a PostgreSQL instance reachable by the JDBC URL and set the environment variables:

- JDBC_DATABASE_URL
- JDBC_DATABASE_USERNAME
- JDBC_DATABASE_PASSWORD

Then run:

	./mvnw spring-boot:run

## Notes

- Hibernate is configured with `spring.jpa.hibernate.ddl-auto=update` for development convenience. For production, prefer explicit migrations (Flyway/Liquibase) and a safer ddl-auto setting.

- The app uses Thymeleaf templates located under `src/main/resources/templates` and static assets under `src/main/resources/static`.
