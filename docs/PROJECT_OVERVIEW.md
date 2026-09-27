# Gatling Home Task - Project Overview

## General Information

**Project:** Gatling Home Task  
**Language:** Java 11  
**Framework:** Gatling 3.13.3  
**Build Tool:** Maven  
**Project Type:** Iterative learning project for load testing an e-commerce application

## Project Structure

```
src/test/java/
├── api/                    # Navigation and API methods
│   └── Navigation.java     # All navigation, cart, checkout, and order methods
├── assertions/            # Performance checks
│   ├── AssertionsMap.java
│   └── PerformanceAssertions.java
├── config/                # Configuration
│   └── BaseHelpers.java  # Constants, HTTP protocol, and system properties
├── scenarios/             # User flow scenarios
│   └── UserFlow.java     # End-to-end e-commerce journey scenario
└── simulations/          # Load testing simulations
    └── PerfTestSimulation.java

src/test/resources/
├── bodies/
│   └── submit.txt        # Template for order submission
├── gatling.conf          # Gatling configuration
└── logback-test.xml      # Logging configuration
```

## Project Evolution by Tasks

The project is built iteratively through 6 tasks, each adding new functionality and concepts.

### Task 1: Scenarios and Simulations

**Objective:** Create a minimal scenario for homepage navigation.

**Implemented:**

- Basic `PerfTestSimulation` with single user
- `UserFlow.userJourney()` - simple navigation scenario
- `Navigation.navigateToHomePage()` - GET request to homepage
  - Text verification
  - Link extraction for navigation
  - Session variable storage

---

### Task 2: HTTP Protocol and Code Structure

**Objective:** Extract constants, add HTTP headers, extend navigation.

**Implemented:**

- `BaseHelpers` - new class with constants:
  - `BASE_URL`
  - `HTTP_PROTOCOL` with HTTP headers
- `PerfTestSimulation` - HTTP protocol configuration added
- `UserFlow` - category navigation added
- `Navigation.navigateToCategory()` - new method for category pages

**Improvements:**

- Code refactoring: URL moved to constant
- Realistic HTTP headers added
- Category navigation extended

---

### Task 3: API Integration and Forms

**Objective:** Implement functionality to add products to cart via API.

**Implemented:**

- `BaseHelpers` - `ADMIN_AJAX` constant added
- `UserFlow` - scenario extended with product navigation and cart operations
- `Navigation.navigateToProduct()` - new method for product pages
- `Navigation.addProductToCart()` - method for adding products to cart via POST requests

**Result:** Complete flow implemented: navigation → category → product → add to cart.

---

### Task 4: Load Configuration and Conditional Logic

**Objective:** Add load parameterization, think time pauses, and conditional logic.

**Implemented:**

- `BaseHelpers` - configuration parameters added (from system properties)
- `PerfTestSimulation` - load configuration added
- `UserFlow` - think time pauses and conditional logic added

**Result:** Full load testing setup with parameterization, think time, and conditional logic.

---

### Task 5: Complex Scenarios and Templates

**Objective:** Implement complete purchase flow to order submission.

**Implemented:**

- `BaseHelpers` - `CART_PERCENTAGE` parameter added
- `PerfTestSimulation` - accepts multiple parameters
- `UserFlow` - checkout flow added with conditional logic
- `Navigation` - cart and checkout navigation methods added
- `Navigation.chooseCountry()` - method for country selection
- `Navigation.placeOrder()` - method for order submission using file templates
- `submit.txt` - template file for order submission

**Result:** Complete purchase flow: cart → checkout → country selection → order submission.

---

### Task 6: Performance Assertions

**Objective:** Add performance assertions to validate application performance.

**Implemented:**

- `BaseHelpers` - `ASSERTION_TYPE` parameter added
- `PerformanceAssertions` - new class with performance assertion sets
- `AssertionsMap` - new class for assertion mapping
- `PerfTestSimulation` - assertions wired into simulation

**Features:**

- Smoke test assertions for basic validation
- E-commerce order assertions with per-group response time checks
- Configurable assertion types via system properties

**Result:** Performance validation with configurable assertion sets.

---

## Configuration Parameters

All parameters are configurable via system properties with default values:

| Parameter           | Default   | Description                                 |
| ------------------- | --------- | ------------------------------------------- |
| `CHAIRS_PERCENTAGE` | 50        | Percentage of users adding chairs           |
| `CART_PERCENTAGE`   | 30        | Percentage of users proceeding to cart      |
| `MIN_THINK_TIME`    | 2         | Minimum pause between actions (seconds)     |
| `MAX_THINK_TIME`    | 5         | Maximum pause between actions (seconds)     |
| `USER_COUNT`        | 100       | Number of virtual users                     |
| `RAMP_UP_PERIOD`    | 20        | Ramp-up duration (seconds)                  |
| `ASSERTION_TYPE`    | smokeTest | Assertion type (smokeTest / ecommerceOrder) |

## Assertion Types

### smokeTest

- Basic validation: ≥99% successful requests

### ecommerceOrder

- 95th percentile response time checks for each request group

## Running Tests

### Performance Test Simulation

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
```

### With Custom Parameters

```bash
mvn gatling:test \
  -Dgatling.simulationClass=simulations.PerfTestSimulation \
  -DUSER_COUNT=200 \
  -DRAMP_UP_PERIOD=30 \
  -DCHAIRS_PERCENTAGE=60 \
  -DCART_PERCENTAGE=40 \
  -DASSERTION_TYPE=ecommerceOrder
```

## Data Structure

### submit.txt

Template file for order submission using session variables (Gatling EL placeholders).

## Final Architecture

The project demonstrates:

1. Basic UI navigation
2. Code refactoring and constant extraction
3. Adding products to cart via API
4. Load testing configuration with parameterization
5. Complete purchase flow (checkout and submit)
6. Performance assertions for validation

**Result:** A fully functional project for load testing an e-commerce application with unified navigation, API methods, and performance validation.
