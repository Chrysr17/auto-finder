# Repository Guidelines

## Project Structure & Module Organization
This repository is a Spring Boot microservices workspace. Each service lives in its own module: `auth-service`, `auto-service`, `comparador-service`, `favorito-service`, and `gateway-service`. Java code is under `src/main/java`, configuration under `src/main/resources`, and tests under `src/test/java`. Shared runtime orchestration is defined in [`docker-compose.yml`](/C:/IntelliJ-projects/auto-finder/docker-compose.yml), and local ports/secrets come from `.env`.

## Build, Test, and Development Commands
Run commands from the target service directory unless noted otherwise.

- `./mvnw clean test` or `mvnw.cmd clean test`: run unit tests for one service.
- `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`: start a service with local config.
- `./mvnw clean package`: build the runnable JAR.
- `docker compose up --build`: build and start the full stack, including MySQL containers.

Example: `cd auto-service && mvnw.cmd clean test`

## Coding Style & Naming Conventions
Use Java 17 and follow the existing Spring style: 4-space indentation, one public class per file, constructor injection, and concise controller/service names. Class names use `PascalCase`; methods and fields use `camelCase`; test methods use descriptive Spanish names such as `agregarFavorito_deberiaGuardarYRetornarDto`. Keep package names lowercase and aligned with the module domain. The project uses Lombok and MapStruct; prefer those existing patterns over manual boilerplate.

## Testing Guidelines
Tests use JUnit 5, Mockito, and Spring Boot Test. Add unit tests beside the affected module under `src/test/java`. Name test classes after the target class, for example `AutoServiceImplTest`. Cover both success paths and failure/validation branches, especially around security filters, Feign clients, and service-layer logic.

## Commit & Pull Request Guidelines
Recent history uses short, scope-first commit messages such as `auto-service modeloServiceTest` and `gateway-service SecurityConfig`. Keep that format: `<module> <change>`, imperative and specific. PRs should describe the behavior change, list touched services, mention config or endpoint impacts, and include sample requests/responses when API behavior changes.

## Security & Configuration Tips
Do not commit real secrets. Keep `JWT_SECRET` and service ports in `.env`, and use `application-local.*` for local overrides and `application-docker.*` for container runs. When changing gateway or JWT behavior, verify downstream services still accept the same token format.

# Memory system

## Reglas de memoria
- Siempre lee memory.md antes de empezar cualquier tarea
- Siempre actualiza memory.md al terminar una tarea importante
- Mantén memory.md claro y resumido