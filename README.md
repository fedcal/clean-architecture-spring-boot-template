# Clean Architecture Spring Boot Template

A 4-layer Clean Architecture Spring Boot 3.4 template (Java 21) with JWT auth,
Flyway migrations, a generic rate-limit slice, a Resilience4j preset, and - the
whole point - an ArchUnit fitness gate that **genuinely runs** (four rules,
`Tests run: 4`, not the silent `Tests run: 0` false-green that the idiomatic
`@ArchTest` static-field pattern produces on Spring Boot 3.4). Sanitized by hand
from a production backend; the example aggregate is a generic `Task`, not any
business vertical.

## Prerequisites

- Java 21 (the build **fails** on newer JDKs used as the compiler default; pin
  `JAVA_HOME` to a JDK 21)
- Maven 3.9+
- PostgreSQL 16 (only needed to actually run the app; the tests do not need it)

```bash
export JAVA_HOME=/path/to/jdk-21
```

## Reproduce

Run this single command to prove the architecture gate really executes:

```bash
mvn test -Dtest=CleanArchitectureTest
```

Expected output: `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0`. If you ever
see `Tests run: 0` for this class, the gate is not protecting anything - that is
exactly the failure mode this template is built to avoid.

To run the whole suite (domain unit tests + the architecture gate), still with no
database and no network:

```bash
mvn test
```

To boot the application (needs a Postgres reachable via the `DB_*` env vars, see
[`.env.example`](./.env.example)):

```bash
mvn spring-boot:run
```

## The four layers

```
presentation  ->  application  ->  domain  <-  infrastructure
```

- `domain` - pure business model (`Task`, `TaskStatus`, the `TaskRepository`
  port). No Spring, no JPA. Enforced by the gate.
- `application` - use cases (`TaskUseCase` / `TaskService`) and the
  `TokenService` port. Depends inward on the domain only.
- `infrastructure` - JPA persistence adapter, JWT implementation, rate-limit
  interceptor, Resilience4j config. Implements the ports.
- `presentation` - REST controllers + exception handler. Depends only on
  application ports, never on infrastructure directly.

## Tests

```bash
mvn test
```

## License

MIT - see [LICENSE](./LICENSE).

## Cite

See [CITATION.cff](./CITATION.cff) for the machine-readable citation (GitHub
renders a "Cite this repository" widget from this file). DOI/Zenodo integration
is deferred - until then, cite via the repository URL and the CITATION.cff
metadata.

## Read the deep-dive

The full write-up (why the ArchUnit gate silently ran zero tests in production,
why I curated the template by hand instead of using `git filter-repo`, and the
declared trade-offs of a broad vs. minimal template) lives at:
[federicocalo.dev/blog/clean-architecture-spring-boot-template-archunit-gate-reale](https://federicocalo.dev/blog/clean-architecture-spring-boot-template-archunit-gate-reale)
(IT) /
[federicocalo.dev/en/blog/clean-architecture-spring-boot-template-archunit-gate-that-runs](https://federicocalo.dev/en/blog/clean-architecture-spring-boot-template-archunit-gate-that-runs)
(EN).
