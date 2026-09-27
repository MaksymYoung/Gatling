# Task 2: HTTP Protocol and Code Structure

## Learning Objectives

By completing this task, you will learn:

- How to structure code using constants and helper classes
- How to configure HTTP protocol with headers
- How to use session variables for dynamic navigation
- Code refactoring best practices

## Overview

In this task, you'll refactor your code to use constants and add HTTP protocol configuration. You'll also extend the scenario to navigate to category pages, learning how to use session variables for dynamic navigation.

## Prerequisites

This task assumes you have completed Task 1. Your `PerfTestSimulation`, `UserFlow`, and `Navigation.navigateToHomePage()` should already be working.

## Task Requirements

### 1. BaseHelpers (New Class)

**Location:** `src/test/java/config/BaseHelpers.java`

**Your task:**

- Create a new class to store constants and configuration
- Add a `BASE_URL` constant set to `"http://localhost"`
- Create an `HTTP_PROTOCOL` builder with realistic HTTP headers:
  - Accept header
  - Accept-Encoding header
  - Accept-Language header
  - Upgrade-Insecure-Requests header
  - User-Agent header (use a modern browser user agent string)

**What you'll learn:**

- How to organize constants in a helper class
- How to configure HTTP protocol in Gatling
- Why HTTP headers matter for realistic load testing

**Hints:**

- Use `HttpDsl.http` to create the protocol builder
- Import `io.gatling.javaapi.http.HttpProtocolBuilder`
- Set headers that mimic a real browser request

### 2. PerfTestSimulation

**Location:** `src/test/java/simulations/PerfTestSimulation.java`

**Your task:**

- Add HTTP protocol configuration to your existing `setUp`
- Use the `HTTP_PROTOCOL` from `BaseHelpers`

**Hints:**

- Use `.protocols()` method on the `setUp` builder
- Import `BaseHelpers` to access `HTTP_PROTOCOL`
- ❌ **Do NOT use** `.withInferHtmlResources()` — it's not needed and wastes resources

### 3. UserFlow

**Location:** `src/test/java/scenarios/UserFlow.java`

**Your task:**

- **Keep existing structure** from Task 1 (flushHttpCache(), flushCookieJar(), exitBlockOnFail())
- Extend your existing scenario to navigate to category pages
- Add navigation steps for "tables" and "chairs" categories
- Use the session variables that were saved in Task 1

**Hints:**

- Add `.exec(navigateToCategory(...))` calls after the homepage navigation
- Use the session variable names you saved in Task 1 ("tables" and "chairs")
- Use descriptive group names like "002_Category" and "003_Category"
- Wrap the navigation execution inside `.exitBlockOnFail().on(...)` to ensure proper fail isolation

### 4. Navigation

**Location:** `src/test/java/api/Navigation.java`

**Your task:**

- **Refactor** `navigateToHomePage()`: Replace hardcoded URL with `BASE_URL` from `BaseHelpers`
- **New method** `navigateToCategory(String group, String sessionVar)`:
  - Make a GET request using a session variable (access first element with `(0)`)
  - Extract a random product link from the category page HTML
  - Save it to a session variable named `"rnd" + sessionVar` (e.g., "rndtables")

**What you'll learn:**

- How to access session variables in URLs using `#{variableName(0)}`
- How to extract random elements from HTML
- How to build dynamic session variable names

**Hints:**

- Use `BASE_URL` from `BaseHelpers` (import it)
- Use `group(group).on(...)` to wrap your HTTP request (required syntax for both methods)
- Access session variables in URLs: `BASE_URL + "#{" + sessionVar + "(0)}"`
- For category navigation, use dynamic request name: `"GET #{" + sessionVar + "(0)}"` to get readable names in reports
- The `.findRandom()` must be inside a `.check()` block for both validation and extraction
- Build the save name dynamically: `"rnd" + sessionVar`

## Testing Your Implementation

1. **Run the simulation:**

   ```bash
   mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
   ```

2. **Verify:**
   - The simulation should complete with 3 requests (homepage, tables category, chairs category)
   - Check that session variables are being used correctly
   - Verify HTTP headers are being sent in requests

## Key Concepts

### Code Organization

Centralizing constants and configuration makes your code more maintainable and easier to modify.

### HTTP Protocol

Configuring HTTP protocol at the simulation level applies settings to all requests, making your tests more realistic.

### Session Variables

Session variables allow you to pass data between requests, enabling dynamic navigation based on previous responses.

## Common Pitfalls

- **Forgetting to import BaseHelpers:** Make sure to import the class to use its constants
- **Incorrect session variable access:** Remember to use `(0)` to access the first element of a list
- **Hardcoded URLs:** Always use constants instead of hardcoding values

## Resources

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Scripting: Best Practices](https://kb.epam.com/pages.viewpage.action?spaceKey=EPMPERF&title=Gatling+Scripting%3A+Best+Practices)
- Review the project's `PROJECT_OVERVIEW.md` for context about the overall project structure

---

**Ready for the next challenge!** 🚀
