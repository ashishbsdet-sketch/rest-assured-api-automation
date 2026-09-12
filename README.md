# Rest Assured API Automation

[![API Tests](https://github.com/ashishbsdet-sketch/rest-assured-api-automation/actions/workflows/api-tests.yml/badge.svg)](https://github.com/ashishbsdet-sketch/rest-assured-api-automation/actions/workflows/api-tests.yml)

This project is a TestNG API regression suite for the public [JSONPlaceholder](https://jsonplaceholder.typicode.com) service. I built it with Java 17 and Rest Assured to show how I structure readable tests, reusable API specifications, typed request models and contract checks.

JSONPlaceholder simulates write operations rather than saving data permanently, which makes it useful for repeatable portfolio tests.

## Coverage

| Area | Checks |
| --- | --- |
| GET by ID | Status, headers, response time, schema and typed deserialization |
| Filtering | Query parameters and collection-level assertions |
| Negative testing | Unknown resource and empty response validation |
| POST | Typed request serialization and created-resource validation |
| PUT | Complete resource replacement |
| PATCH | Partial resource update |
| DELETE | Status and response-body validation |
| Data-driven testing | Multiple post IDs through a parallel TestNG data provider |

## Project structure

```text
.
├── .github/workflows/       # CI pipeline
├── src/test/java/dev/ashish/qa/
│   ├── config/              # Environment configuration
│   ├── data/                # TestNG data providers
│   ├── models/              # Request and response records
│   ├── specs/               # Reusable Rest Assured specifications
│   └── tests/               # Read and write API scenarios
├── src/test/resources/
│   └── schemas/             # JSON contract schemas
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

- Request and response specifications keep common protocol checks in one place.
- Java records make API payloads explicit without adding boilerplate.
- TestNG groups separate smoke, regression, contract and negative coverage.
- The data provider exercises multiple resources without copying test methods.
- Logging is enabled only when validation fails, which keeps normal CI output readable.
- The suite checks response time, but uses a generous limit to avoid treating public-internet variation as a product defect.
- Tests do not depend on execution order or data created by another test.

## Reports and CI

Maven Surefire produces TestNG and JUnit-compatible results under `target/surefire-reports/`. GitHub Actions runs the complete suite on every pull request and push to `main`, then retains the reports as downloadable artifacts.

## Configuration

The base URL is resolved in this order:

1. Maven property: `-Dbase.url=...`
2. Environment variable: `BASE_URL`
3. Default JSONPlaceholder URL

## Next steps

Useful future additions would be authenticated API coverage, token refresh handling, database verification and consumer-driven contract testing. They are intentionally not simulated here because this public API does not provide those behaviours.

## Disclaimer

JSONPlaceholder is a public test service. This repository is an independent portfolio project.
