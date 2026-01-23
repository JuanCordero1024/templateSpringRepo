# TemplateSpringRepo-EcoSign

TemplateSpringRepo-EcoSign is a Spring-based template repository aimed at implementing a hybrid layers + microservices architecture for an e-signature platform (EcoSign). The repository contains multiple branches, each implementing a single responsibility (single microservice or domain layer), such as authentication, file storage, user CRUD, roles, and signature processing. The project includes Docker and Maven build support with a Makefile and Docker Compose for local integration.

This *README* gives an overview, quick start instructions, architecture guidance, branch mapping and recommended next steps.

## Goals
- Provide a template to build small Spring-based microservices (or layered modules) for an e-signature platform.
- Keep each branch/small service focused on a single responsibility (single responsibility principle).
- Offer a base for ACID-capable modules (user CRUD, roles) and stateless microservices (file service, auth tokens, signature processor).
- Provide Dockerized development and Compose for local end-to-end runs.

## Architecture (high-level)
- **Hybrid**: the repo aims to support both:
    - Layered modules within a service (Controller -> Service -> Repository)
    - Independent microservices for cross-cutting domains (auth, files, signature)
- Each branch implements a single responsibility:
    - Transactional/ACID services: user CRUD, role service (persist data in a relational DB with transactions)
    - Stateless or file-based services: file microservice (file storage), auth microservice (token issuance/validation), signature microservice (document signing)
- Typical packages:
    - controller (REST endpoints)
    - service (business logic + transactions)
    - repository (Spring Data JPA / persistence)
    - dto (data transfer objects)
    - config (security, datasource, properties)

## Technologies
- Java 21
- Spring Boot 3.3 (web, feign, data-jpa, security, etc.)
- Maven (pom.xml) + mvnw wrapper
- Docker & Docker Compose (provided Dockerfile and Docker-compose.yml)
- Makefile for convenience tasks
- Relational DB PostgreSQL

## Quick start (local)
1. Verify environment: Java, Docker, Docker Compose, Maven (if not using wrapper)
2. If you want to run everything via Docker Compose:
    - docker-compose up --build
    - (The Docker-compose.yml in the repo should orchestrate database + services; check and adapt env vars)
3. To run a single module locally:
    - Checkout the branch (for example `feat/authMicroService`)
    - Inspect `src/main` and run via:
        - ./mvnw spring-boot:run
        - or build: ./mvnw -DskipTests package && java -jar target/<artifact>.jar
4. Use the Makefile for convenience if it provides tasks (check available targets).

Environment variables to confirm/setup (common):
- SPRING_PROFILES_ACTIVE (dev/prod)
- SPRING_DATASOURCE_URL / USER / PASSWORD
- JWT_SECRET (for auth service)
- FILE_STORAGE_PATH or S3 configuration
- PORTs for each microservice

## Branch mapping and short purpose
(Full branch-specific READMEs are included in this proposal — see branch README files below.)

- feat/authMicroService
    - Responsibility: authentication and authorization (token issuance, validation, user sessions).
    - Likely stateless w/ token signing and optional refresh tokens.

- feat/crud-user
    - Responsibility: user management CRUD with persistent storage and transactional guarantees (ACID).
    - Expected to be the authoritative source for user data.

- feat/roleService
    - Responsibility: role & permissions management, likely transactional and ACID.

- feat/files-microService
    - Responsibility: file upload/download, storage abstraction (local filesystem or object store), metadata persistence.

- feat/Microservice_firma (and fear/Microservice_firma)
    - Responsibility: signature processing microservice — apply and verify electronic signatures on documents.

- main
    - Canonical base branch. Use for release-ready composition, is the main template of the other ones.

## API Gateway and usage on environment variables

Some of the branches got an special environment variables, this ones requieres the following repo: 
- [View Repo](https://github.com/JuanCordero1024/TheWorkersAPIGateway)
