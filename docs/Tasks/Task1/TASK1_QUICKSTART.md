# Task 1: Scenarios and Simulations

<div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 24px 28px; border-radius: 12px; margin: 24px 0; box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);">

<div style="display: flex; align-items: center; gap: 12px; margin-bottom: 16px;">
  <span style="font-size: 2em;">⚡</span>
  <span style="font-size: 1.3em; font-weight: bold;">Important for All Students</span>
</div>

<div style="background: rgba(255, 255, 255, 0.15); padding: 16px; border-radius: 8px; margin: 16px 0; backdrop-filter: blur(10px); border: 1px solid rgba(255, 255, 255, 0.2);">
  <div style="font-size: 1.1em; font-weight: bold; margin-bottom: 12px; text-shadow: 0 2px 4px rgba(0,0,0,0.2);">
    Please follow the code structure!
  </div>
  <div style="line-height: 1.6; opacity: 0.95;">
    The project already contains skeleton files that need to be filled in. Do not create new files from scratch — use the existing structure and fill in the ready-made templates.
  </div>
</div>

</div>

> **📖 Full task description:** For detailed instructions, hints, key concepts explanations, and additional resources, see the `TASK1_README.md` file located at `docs/Tasks/Task1/`.

## Overview

Create a minimal load testing scenario that navigates to the homepage of an e-commerce application.

## Files to Implement

```
src/test/java/
├── api/Navigation.java
├── scenarios/UserFlow.java
└── simulations/PerfTestSimulation.java
```

## Required Static Imports

```java
import static io.gatling.javaapi.core.CoreDsl.*;  // scenario(), exec(), group()
import static io.gatling.javaapi.http.HttpDsl.*;  // http(), flushHttpCache(), flushCookieJar()
```

## Tasks

### 1. PerfTestSimulation.java

- Configure `setUp()` to use `UserFlow.userJourney()`
- Set load injection: 1 user at once using `injectOpen(atOnceUsers(1))`
- Class must extend `Simulation`

### 2. UserFlow.java

- Create scenario named "E-commerce End-to-End Journey"
- Clear HTTP cache and cookies before starting
- Add navigation step: call `Navigation.navigateToHomePage("001_HomePage")`
- Use `exitBlockOnFail()` to stop on failure

### 3. Navigation.java

- Implement `navigateToHomePage(String group)` method
- Make GET request to `http://localhost` (add TODO comment: `// TODO: move to BaseHelpers later`)
- Request name must follow format "GET /" or "{METHOD} {ENDPOINT}"\*\* (e.g., `http("GET /")`)
- Verify page contains text "Performance testing Essentials" (1 check)
- Extract navigation links (Tables, Chairs, Cart, Checkout) from HTML using regex (4 checks)
- Total: 5 separate `.check()` calls required
- Save extracted links in session variables: `tables`, `chairs`, `cart`, `checkout`
- Use `group(group).on(...)` for request grouping (required syntax)

## Testing

Run the simulation:

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
```

Expected results:

- One successful request to homepage
- Session variables saved correctly
- Report generated in `target/gatling`
