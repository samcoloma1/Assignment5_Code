[![.github/workflows/SE333_CI.yml](https://github.com/samcoloma1/Assignment5_Code/actions/workflows/SE333_CI.yml/badge.svg)](https://github.com/samcoloma1/Assignment5_Code/actions/workflows/SE333_CI.yml)

Assignment 5 — Unit, Mocking, and Integration Testing

This repo contains tests and CI for the Barnes & Noble and Amazon examples.

What’s inside
- Part 1: Barnes & Noble tests 
  - added tests covering both specification-based & structural-based
  - establishes the testing foundation for the project
- Part 2: GitHub Actions workflow + Checkstyle + JaCoCo + status badge
  - Configured `.github/workflows/SE333_CI.yml` to automatically run on each push to `main`:
  - Runs Checkstyle
  - Runs JUnit tests
  - Generates JaCoCo coverage report
  - Uploads:
      - `checkstyle.xml`
      - `jacoco.xml`
  - Display build status badge

- Part 3: Amazon unit & integration tests
  - Both unit and integration tests were implemented
  - Each contains both specification and structural based tests

Latest GitHub Actions workflow completed successfully and produced checkstyle.xml and jacoco.xml.
Earlier runs show failures before CI was fully configured in part 2.

Jacoco coverage is in target/site/jacoco/index.html

Build & Test
```bash
mvn -q -B clean verify
