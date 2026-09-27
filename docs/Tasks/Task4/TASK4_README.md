# Task 4: Load Configuration and Conditional Logic

## Learning Objectives

By completing this task, you will learn:

- How to parameterize load testing through system properties
- How to add think time pauses for realistic user behavior
- How to implement conditional logic in scenarios

## Overview

In this task, you'll transform your simple test into a proper load test with configurable parameters and realistic user behavior. You'll learn how to make your tests configurable and model different user paths.

## Prerequisites

This task assumes you have completed Task 3. Your scenario should include navigation, product viewing, and adding items to cart.

> **📝 Note:** This task extends the structure from Task 3. The `api.Navigation` class (updated in Task 3) must exist. Task 4 only adds configuration and conditional logic to the existing flow.

## Task Requirements

### 1. BaseHelpers

**Location:** `src/test/java/config/BaseHelpers.java`

**Your task:**

- Add configuration constants that read from system properties with default values:
  - `CHAIRS_PERCENTAGE` (default: 50) - percentage of users who add chairs
  - `MIN_THINK_TIME` (default: 2) - minimum pause between actions (seconds)
  - `MAX_THINK_TIME` (default: 5) - maximum pause between actions (seconds)
  - `USER_COUNT` (default: 100) - number of virtual users
  - `RAMP_UP_PERIOD` (default: 20) - ramp-up duration (seconds)

**What you'll learn:**

- How to make tests configurable without code changes
- How to use system properties for test parameters

**Hints:**

- Use `System.getProperty("KEY", "defaultValue")`
- Parse strings to appropriate types (`Double.parseDouble()`, `Integer.parseInt()`)

### 2. PerfTestSimulation

**Location:** `src/test/java/simulations/PerfTestSimulation.java`

**Your task:**

- Change load injection from `atOnceUsers(1)` to `userJourney(CHAIRS_PERCENTAGE).injectOpen(rampUsers(USER_COUNT).during(RAMP_UP_PERIOD))`
- Pass `CHAIRS_PERCENTAGE` as parameter to `userJourney()`

**What you'll learn:**

- How to configure realistic load patterns
- How to scale tests with configurable user counts

**Hints:**

- Use `rampUsers()` for gradual load increase
- Import constants from `BaseHelpers`

### 3. UserFlow

**Location:** `src/test/java/scenarios/UserFlow.java`

**Your task:**

- Modify `userJourney()` to accept a `Double chairs` parameter
  - ⚠️ **Important:** The parameter name in the method signature must match the name used in `percent()`. If you name the parameter `chairs`, use `percent(chairs)`. If you name it `chairsProbability`, use `percent(chairsProbability)`.
- Add `.pause(MIN_THINK_TIME, MAX_THINK_TIME)` between each step
- Add conditional logic: only some users (based on `chairs` percentage) should add chairs to cart
- Use `randomSwitch().on(percent(chairs).then(...))` for conditional execution
- Chairs branch must contain full sequence: navigateToCategory → navigateToProduct → addProductToCart, all with pauses between steps
- **Note:** Your scenario should start with `flushHttpCache()` and `flushCookieJar()`

**What you'll learn:**

- How to add think time for realistic behavior
- How to implement conditional logic in scenarios
- How to model different user behaviors

**Hints:**

- Import `MIN_THINK_TIME` and `MAX_THINK_TIME` from `BaseHelpers`
- Use `.randomSwitch()` with `.on(percent(...).then(...))`
- Only the chairs-related steps should be conditional
- Wrap the navigation execution inside `.exitBlockOnFail().on(...)` to ensure proper fail isolation
- All steps must use `group(group).on(...)` for request grouping (required syntax)
- All steps must include at least one `.check(...)` using `regex()` for validation
- Add pauses inside the Chairs branch between each step (navigateToCategory, navigateToProduct, addProductToCart)

## Testing Your Implementation

1. **Run with default parameters:**

   ```bash
   mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
   ```

2. **Run with custom parameters:**

   ```bash
   mvn gatling:test \
     -Dgatling.simulationClass=simulations.PerfTestSimulation \
     -DUSER_COUNT=50 \
     -DCHAIRS_PERCENTAGE=30
   ```

3. **Verify:**
   - Load should ramp up gradually
   - Only specified percentage of users should add chairs
   - Think time pauses should be visible between actions

## Key Concepts

### Load Parameterization

Making tests configurable allows you to run different load patterns without changing code.

### Think Time

Pauses between actions simulate real user behavior, making tests more realistic.

### Conditional Logic

Not all users follow the same path - conditional logic models real-world scenarios.

## Common Pitfalls

- **Forgetting to import constants:** Make sure to import from `BaseHelpers`
- **Wrong percentage calculation:** Make sure percentages are between 0 and 100

## Resources

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Scripting: Best Practices](https://kb.epam.com/pages.viewpage.action?spaceKey=EPMPERF&title=Gatling+Scripting%3A+Best+Practices)
- Review the project's `PROJECT_OVERVIEW.md` for context about the overall project structure

---

**Your load test is now production-ready!** 📊
