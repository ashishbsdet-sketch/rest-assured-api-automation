# API Test Strategy

## Purpose

This suite provides fast feedback on the public posts and comments contracts exposed by JSONPlaceholder. It is designed as a portfolio example of maintainable API automation rather than a claim that a public demo service needs production monitoring.

## Test layers

| Layer | Purpose | Examples |
| --- | --- | --- |
| Smoke | Confirm critical endpoints are available | Read a post, create a post, read comments |
| Contract | Detect response-shape changes | JSON Schema, required fields, content type |
| Functional | Validate endpoint behaviour | Filtering, POST, PUT, PATCH and DELETE |
| Negative | Check missing-resource behaviour | Unknown post and unknown comment collection |
| Consistency | Compare equivalent API routes | Nested comments and filtered comments |

## Execution model

Tests run as independent TestNG classes with two worker threads. They do not share mutable data or depend on execution order. The public service simulates writes, so POST, PUT, PATCH and DELETE tests validate the immediate response rather than a later database read.

## Quality gates

A change is ready to merge when:

1. Maven resolves dependencies from a clean workspace.
2. Every TestNG test passes.
3. JSON contracts remain valid.
4. No test relies on another test's output.
5. Test reports are uploaded by CI.

## Risks and controls

- Public service latency can vary, so the response-time threshold is intentionally generous.
- Public data may be refreshed, so assertions focus on stable IDs, ownership and contracts rather than full text.
- JSONPlaceholder does not provide authentication, rate limits or persistence. Those capabilities are listed as future work instead of being mocked and presented as real coverage.
- Parallel data-provider tests only perform reads, avoiding shared write-state problems.

## What belongs in a real project

For a production service, this design would also include protected credentials, token refresh, environment-specific configuration, trace or correlation IDs, database or event verification, consumer-driven contracts, and service-level performance tests.
