# Assetly

A backend for storing and sharing design assets, like a small version of what sits behind a tool such as Canva. It has no UI, only an API that other programs talk to.

> **Personal learning project.** I'm an experienced PHP/Symfony backend developer building Assetly to learn Java and its ecosystem. It is not production software, and any performance numbers in this repo will be ones I have measured myself.

## What it will do

1. **Upload an asset**, such as a logo or a photo. The system records its name, type, size and owner.
2. **Organise assets in folders**, like a file explorer.
3. **Generate thumbnails automatically.** The upload returns immediately and a background worker creates the thumbnail afterwards.
4. **Share an asset through a link** that other people can open.

The thumbnail step is the central design idea. Resizing images inside the upload request would keep the user waiting, so the API puts a job on a queue and a separate worker picks it up. Most of the interesting work in this project, such as concurrency, retries and queues, is about doing that reliably.

## Roadmap

Each stage ends with something that runs, is tested and is committed.

| Stage | What exists and runs | Status |
|-------|----------------------|--------|
| M0 | Plain-Java domain core: records, in-memory repository, a CLI with JSON storage, an LRU cache | In progress |
| M1 | A concurrent job processor written from scratch: worker pool, retries with backoff, graceful shutdown, thumbnails with ImageIO | Planned |
| M2 | REST API on Spring Boot 3 with PostgreSQL, Flyway and Testcontainers integration tests | Planned |
| M3 | JWT login, BCrypt, ownership checks and a hand-written token-bucket rate limiter | Planned |
| M4 | Uploads to S3, jobs on SQS, a separate worker service, idempotency keys, a dead-letter queue, share links in DynamoDB | Planned |
| M5 | Docker Compose, CI, health checks, metrics, structured logs, OpenAPI | Planned |
| M6 | k6 load test, profiling, and one measured performance fix with before and after numbers | Planned |

## Tech stack

Java 21, Maven, JUnit 5. Planned for later stages: Mockito, Spring Boot 3, PostgreSQL, Flyway, Testcontainers, LocalStack, AWS (S3, SQS, DynamoDB), Docker Compose, GitHub Actions and Micrometer.

## Project layout

```
assetly/
├── pom.xml   parent Maven build (Java 21)
└── core/     plain-Java domain model, no framework dependencies
```

Planned modules: `api-service/` (Spring Boot REST API), `worker-service/` (thumbnail worker), `docs/` (architecture diagram and decision records) and `.github/workflows/` (CI). The `core` module stays framework-free and is a dependency of both services.

## Getting started

Requirements: JDK 21 and Maven.

```bash
git clone https://github.com/fatemehbarati/assetly.git
cd assetly
mvn test
```

## Learning log

Notes from each session are kept in [LEARNINGS.md](LEARNINGS.md).
