# Task 2: HTTP Protocol and Code Structure

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

> **📖 Full task description:** For detailed instructions, hints, key concepts explanations, and additional resources, see the `TASK2_README.md` file located at `docs/Tasks/Task2/`.

## Overview

Refactor code to use constants, add HTTP protocol configuration, and extend scenario to navigate category pages.

## Files to Modify/Create

```
src/test/java/
├── config/BaseHelpers.java          (NEW)
├── simulations/PerfTestSimulation.java
├── scenarios/UserFlow.java
└── api/Navigation.java
```

## Tasks

### 1. BaseHelpers.java (NEW)

- Create class with `BASE_URL = "http://localhost"`
- Create `HTTP_PROTOCOL` with realistic headers:
  - Accept, Accept-Encoding, Accept-Language
  - Upgrade-Insecure-Requests
  - User-Agent (modern browser string)

### 2. PerfTestSimulation.java

- Add `.protocols(HTTP_PROTOCOL)` to `setUp()`
- Import `BaseHelpers.HTTP_PROTOCOL`
- ❌ **Do NOT use** `.withInferHtmlResources()` — it's not needed and wastes resources

### 3. UserFlow.java

- **Keep existing structure** from Task 1 (flushHttpCache(), flushCookieJar(), exitBlockOnFail())
- Wrap execution inside `.exitBlockOnFail().on(...)` (required syntax)
- Add navigation steps for "tables" and "chairs" categories
- Use session variables from Task 1: `"tables"` and `"chairs"`
- Call `Navigation.navigateToCategory()` with group names "002_Category", "003_Category"

### 4. Navigation.java

- **Refactor** `navigateToHomePage()`: Replace hardcoded URL with `BaseHelpers.BASE_URL`
- Use `group(group).on(...)` for request grouping (required syntax for both methods)
- **New method** `navigateToCategory(String group, String sessionVar)`:
  - GET request using `#{sessionVar(0)}` from session
  - Use dynamic request name: `"GET #{" + sessionVar + "(0)}"` for better reporting
  - Extract random product link from category page using `.check(regex(...).findRandom().saveAs(...))`
  - Save to session variable: `"rnd" + sessionVar` (e.g., "rndtables")

## Testing

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
```

Expected: 3 requests (homepage, tables, chairs), HTTP headers sent, session variables used correctly.
