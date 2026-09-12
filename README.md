# Rest Assured API Automation

[![API Tests](https://github.com/ashishbsdet-sketch/rest-assured-api-automation/actions/workflows/api-tests.yml/badge.svg)](https://github.com/ashishbsdet-sketch/rest-assured-api-automation/actions/workflows/api-tests.yml)

This project is a TestNG API regression suite for the public [JSONPlaceholder](https://jsonplaceholder.typicode.com) service. I built it with Java 17 and Rest Assured to show how I structure readable tests, reusable API clients, shared specifications, typed models and contract checks.

JSONPlaceholder simulates write operations rather than saving data permanently, which makes it useful for repeatable portfolio tests.

## Coverage

| Area | Checks |
| --- | --- |
| Posts | Read, filter, create, replace, update and delete |
| Comments | Nested routes, query filtering, email format and route consistency |
| Contracts | Object and collection JSON Schemas |
| Protocol | Status codes, content type and response time |
| Negative testing | Unknown resource and empty-collection behaviour |
| Data-driven testing | Multiple post IDs through a parallel TestNG data provider |

## Project structure

```text
.
├── .github/                  # CI, dependency updates and PR template
├── docs/
│   └── TEST_STRATEGY.md      # Scope, risks and quality gates
├── src/test/java/dev/ashish/qa/
│   ├── clients/              # Endpoint operations
│   ├── config/               # Environment configuration
│   ├── data/                 # TestNG data providers
│   ├── models/               # Typed request and response records
│   ├── specs/                # Shared Rest Assured specifications
│   └── tests/                # Behaviour and contract assertions
├── src/test/resources/
│   └── schemas/              # JSON contracts
├── pom.xml
└── testng.xml
```

## Run locally

Prerequisites: Java 17+ and Maven 3.9+.

```bash
git clone https://github.com/ashishbsdet-sketch/rest-assured-api-automation.git
cd rest-assured-api-automation
mvn clean test
```

Run against another environment:

```bash
mvn clean test -Dbase.url=https://your-api.example.com
```

Run only the smoke group:

```bash
mvn test -Dgroups=smoke
```

## Design choices

- Client classes separate endpoint calls from test assertions.
- Shared specifications keep protocol checks consistent.
- Java records make API payloads explicit without adding boilerplate.
- TestNG groups separate smoke, regression, contract and negative coverage.
- Data providers exercise multiple resources without copying test methods.
- Logging is enabled only when validation fails, keeping CI output readable.
- Tests do not depend on execution order or data created elsewhere.

The detailed scope, risks and merge gates are documented in [the test strategy](docs/TEST_STRATEGY.md).

## Reports and CI

Maven Surefire produces TestNG and JUnit-compatible results under `target/surefire-reports/`. GitHub Actions runs the suite on every pull request and push to `main`, then retains the reports as downloadable artifacts.

## Configuration

The base URL is resolved in this order:

1. Maven property: `-Dbase.url=...`
2. Environment variable: `BASE_URL`
3. Default JSONPlaceholder URL

## Known limitation

JSONPlaceholder does not persist writes and does not provide authentication or rate-limit behaviour. The suite validates immediate write responses and does not pretend to verify unsupported capabilities.

## Next steps

The next meaningful extension would use a service that supports protected authentication, token refresh and persistent test data. Database verification and consumer-driven contracts would then be added where the architecture supports them.

## Disclaimer

JSONPlaceholder is a public test service. This repository is an independent portfolio project.
