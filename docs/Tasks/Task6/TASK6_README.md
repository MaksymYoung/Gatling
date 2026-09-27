# Task 6: Performance Assertions

## Learning Objectives

By completing this task, you will learn:

- How to define performance criteria for load tests
- How to create reusable assertion sets
- How to use percentiles for response time validation
- How to validate performance at different levels (global and per-group)
- How to wire assertions into simulations

## Overview

In this task, you'll add performance assertions to validate that your application meets performance requirements. You'll learn how to define different types of assertions for different testing purposes and how to make them configurable.

## Prerequisites

This task assumes you have completed Task 5. Your scenario should have configurable load, think time, and conditional logic.

## Task Requirements

### 1. BaseHelpers

**Location:** `src/test/java/config/BaseHelpers.java`

**Your task:**

- Add `ASSERTION_TYPE` constant (default: "smokeTest") - type of assertions to use

**What you'll learn:**

- How to make assertions configurable

**Hints:**

- Use `System.getProperty("ASSERTION_TYPE", "smokeTest")`

### 2. PerformanceAssertions (New Class)

**Location:** `src/test/java/assertions/PerformanceAssertions.java`

**Your task:**

- Create constants: `SMOKE_TEST = "smokeTest"` and `ECOMMERCE_ORDER = "ecommerceOrder"`
- Create `smokeTestAssertions`: basic validation (≥99% successful requests)
- Create `ecommerceOrderAssertions`: 95th percentile response time checks for each request group

**What you'll learn:**

- How to define performance criteria
- How to use percentiles for response time validation
- Different types of assertions for different test purposes

**Hints:**

- Use `global().successfulRequests().percent().gte(99.0)` for smoke test
- Use `details("groupName").responseTime().percentile3().lte(milliseconds)` for specific checks
- percentile3() is the 95th percentile
- Set reasonable thresholds based on your application
- Group names in assertions must exactly match group names used in the scenario

**Example group names to check:**

- "001_HomePage"
- "002_Category"
- "003_Tables"
- "004_Tables_addToCart"
- "005_Category"
- "006_Chairs"
- "007_Chairs_addToCart"
- ... and so on (include all groups from your scenario)

### 3. AssertionsMap (New Class)

**Location:** `src/test/java/assertions/AssertionsMap.java`

**Your task:**

- Create a Map that links assertion type names to assertion lists
- Map `SMOKE_TEST` to `smokeTestAssertions`
- Map `ECOMMERCE_ORDER` to `ecommerceOrderAssertions`

**What you'll learn:**

- How to organize assertions for reusability
- How to make assertions configurable

**Hints:**

- Use `Map.of()` to create the mapping
- Import assertion lists from `PerformanceAssertions`

### 4. PerfTestSimulation

**Location:** `src/test/java/simulations/PerfTestSimulation.java`

**Your task:**

- Add assertions using `ASSERT_MAPPING.get(ASSERTION_TYPE)`
- Import `AssertionsMap.ASSERT_MAPPING`

**What you'll learn:**

- How to wire assertions into simulations
- How to make assertions configurable

**Hints:**

- Use `.assertions()` method on setUp
- Import `static assertions.AssertionsMap.ASSERT_MAPPING`

## Testing Your Implementation

1. **Run with smoke test assertions (default):**

   ```bash
   mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
   ```

2. **Run with e-commerce order assertions:**

   ```bash
   mvn gatling:test \
     -Dgatling.simulationClass=simulations.PerfTestSimulation \
     -DASSERTION_TYPE=ecommerceOrder
   ```

3. **Verify:**
   - Assertions should validate performance
   - Test should fail if performance requirements are not met
   - Different assertion types should validate different criteria

## Key Concepts

### Performance Assertions

Assertions validate that your application meets performance requirements. They can check:

- Global metrics (all requests)
- Per-group metrics (specific request groups)
- Success rates
- Response times (using percentiles)

### Percentiles

Percentiles help you understand response time distribution:

- percentile3() = 95th percentile (p95)
- This means 95% of requests completed within this time

### Assertion Types

Different assertion types serve different purposes:

- **smokeTest**: Quick validation that the application is working
- **ecommerceOrder**: Detailed validation of performance for each step

## Common Pitfalls

- **Group name mismatch**: Assertion group names must exactly match scenario group names
- **Wrong percentile:** percentile3() is 95th, not 50th
- **Too strict assertions:** Set realistic thresholds based on your application
- **Missing assertions wiring:** Don't forget to add `.assertions()` to simulation

## Resources

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Assertions](https://gatling.io/docs/gatling/reference/current/core/assertions/)
- [Gatling Scripting: Best Practices](https://kb.epam.com/pages.viewpage.action?spaceKey=EPMPERF&title=Gatling+Scripting%3A+Best+Practices)
- Review the project's `PROJECT_OVERVIEW.md` for context about the overall project structure

---

**Your load test now validates performance!** 📊
