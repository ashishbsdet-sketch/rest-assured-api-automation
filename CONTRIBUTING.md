# Contributing

Keep changes small enough to review and include the reason for adding or changing a test.

Before opening a pull request:

```bash
mvn clean test
```

When adding coverage:

- put endpoint calls in a client class
- keep protocol defaults in the shared specifications
- use typed models for stable request and response bodies
- add or update a JSON Schema when the public contract changes
- avoid test-order dependencies and fixed waits
- use TestNG groups when a test belongs to smoke, contract, regression or negative coverage
- document public-service limitations instead of hiding them with retries

A pull request should explain what behaviour is covered, how it was checked, and any known limitation.
