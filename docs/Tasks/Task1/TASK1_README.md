# Task 1: Scenarios and Simulations

## Learning Objectives

By completing this task, you will learn:

- How to create your first Gatling simulation
- How to build a basic scenario with HTTP requests
- How to extract data from HTML responses using regular expressions
- How to work with session variables to store extracted data
- The fundamental structure of a Gatling performance test

## Overview

In this first task, you will create a minimal load testing scenario that navigates to the homepage of an e-commerce application. This will introduce you to the core concepts of Gatling: simulations, scenarios, and HTTP requests.

## Project Structure

You will be working with three main files:

```
src/test/java/
├── api/
│   └── Navigation.java          # Navigation methods
├── scenarios/
│   └── UserFlow.java            # User journey scenario
└── simulations/
    └── PerfTestSimulation.java  # Main simulation class
```

## Important: Static Imports

In Gatling Java API, many methods are static and require static imports. You'll need:

- `import static io.gatling.javaapi.core.CoreDsl.*;` - for `scenario()`, `exec()`, `group()`, etc.
- `import static io.gatling.javaapi.http.HttpDsl.*;` - for `http()`, `flushHttpCache()`, `flushCookieJar()`, etc.

These imports allow you to use methods like `scenario()` and `http()` directly without a class prefix.

## Task Requirements

### 1. PerfTestSimulation

**Location:** `src/test/java/simulations/PerfTestSimulation.java`

**Your task:**

- Configure the `setUp` method to use `UserFlow.userJourney()`
- Set up the load injection to run with 1 user at once
- This is where you define which scenario to run and how many virtual users will execute it

**Hints:**

- Look for the `setUp()` method in Gatling's Simulation class
- Use `injectOpen()` with `atOnceUsers()` for a simple single-user test
- The simulation class should extend `Simulation`

### 2. UserFlow

**Location:** `src/test/java/scenarios/UserFlow.java`

**Your task:**

- Create a scenario named "E-commerce End-to-End Journey"
- Add steps to clear HTTP cache and cookies before starting
- Add a navigation step to the homepage
- Use `exitBlockOnFail()` to ensure the scenario stops if any step fails

**What you'll learn:**

- How to structure a Gatling scenario
- The importance of clearing cache and cookies for realistic testing
- How to chain multiple steps together using `.exec()`

**Hints:**

- You'll need to import static methods from `io.gatling.javaapi.core.CoreDsl` to use `scenario()`
- Use `scenario("name")` to create a new scenario builder (requires static import)
- Look for `flushHttpCache()` and `flushCookieJar()` methods (also require static imports from `HttpDsl`)
- The navigation step should call `navigateToHomePage()` with a group name like "001_HomePage"
- You'll also need static imports from `io.gatling.javaapi.http.HttpDsl` for HTTP-related methods

### 3. Navigation

**Location:** `src/test/java/api/Navigation.java`

**Your task:**

- Implement the `navigateToHomePage(String group)` method
- Make a GET HTTP request to `http://localhost`
- Request name must follow format "GET /" or "{METHOD} {ENDPOINT}"\*\* (e.g., `http("GET /")`)
- Add a TODO comment: `// TODO: move to BaseHelpers later` above the hardcoded URL
- Verify that the page contains the text "Performance testing Essentials"
- Extract navigation links for Tables, Chairs, Cart, and Checkout from the HTML
- Save the extracted links in session variables for later use

**What you'll learn:**

- How to make HTTP GET requests in Gatling
- How to use regular expressions to extract data from HTML responses
- How to use `.check()` methods to validate responses
- How to save extracted data to session variables using `.saveAs()`
- The concept of request grouping for better reporting

**Hints:**

- Use `group(group).on(...)` to create a named group for this request (the group() call must use this syntax to wrap your HTTP request)
- Use `http()` to create an HTTP request builder with a descriptive name following format "GET /" or "{METHOD} {ENDPOINT}" (e.g., `http("GET /")`)
- Use `.get()` to specify the URL
- Use `.check()` with `regex()` to find patterns in the response
- Use `.findAll()` to extract multiple matches
- Use `.saveAs()` to store values in session variables
- Look for anchor tags (`<a>`) in the HTML that contain the navigation links

**Expected session variables:**
After this method executes, your session should contain:

- `tables` - list of table category links
- `chairs` - list of chair category links
- `cart` - cart page link
- `checkout` - checkout page link

## Testing Your Implementation

Once you've completed the implementation:

1. **Run the simulation:**

   ```bash
   mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
   ```

2. **Check the results:**

   - The simulation should complete successfully
   - You should see a report in the `target/gatling` directory
   - The report should show one successful request to the homepage
   - Check that session variables are being saved correctly

3. **Verify the checks:**
   - The page should contain "Performance testing Essentials"
   - Navigation links should be extracted and saved

## Key Concepts

### Simulations

A simulation is the main entry point for a Gatling test. It defines which scenarios to run and how to inject load.

### Scenarios

A scenario describes a sequence of actions that a virtual user will perform. It's like a script that defines user behavior.

### HTTP Requests

HTTP requests are the core of web application load testing. You'll use them to interact with the application under test.

### Checks

Checks allow you to validate responses and extract data. They're essential for ensuring your test is working correctly and for extracting data needed in subsequent requests.

### Session Variables

Session variables store data that can be used across multiple requests in a scenario. They're crucial for building realistic user journeys.

### Request Grouping

Grouping requests helps organize your test reports and makes it easier to analyze performance for specific parts of your application.

## Common Pitfalls

- **Forgetting to import required classes:** Make sure you import all necessary Gatling classes
- **Incorrect regex patterns:** Test your regular expressions to ensure they match the HTML structure
- **Not saving session variables:** Remember to use `.saveAs()` to store extracted data
- **Missing checks:** Always verify that your requests are successful

## Next Steps

After completing this task, you'll be ready to:

- Add more navigation steps
- Extract and use data from responses
- Build more complex user journeys

## Resources

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Scripting: Best Practices](https://kb.epam.com/pages/viewpage.action?spaceKey=EPMPERF&title=Gatling+Scripting%3A+Best+Practices)
- Review the project's `PROJECT_OVERVIEW.md` for context about the overall project structure

---

**Good luck with your first Gatling simulation!** 🚀
