# Task 4: Load Configuration and Conditional Logic

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

> **📖 Full task description:** For detailed instructions, hints, key concepts explanations, and additional resources, see the `TASK4_README.md` file located at `docs/Tasks/Task4/`.

## Overview

Add configurable load parameters, think time, and conditional logic.

## Files to Modify/Create

```
src/test/java/
├── config/BaseHelpers.java
├── simulations/PerfTestSimulation.java
└── scenarios/UserFlow.java
```

> **📝 Note:** This task extends the structure from Task 3. The `api.Navigation` class (created in Task 3) must exist. Task 4 only adds configuration and conditional logic to the existing flow.

## Tasks

### 1. BaseHelpers.java

- Add configurable constants (read from system properties with defaults):
  - `CHAIRS_PERCENTAGE` (default: 50)
  - `MIN_THINK_TIME` (default: 2)
  - `MAX_THINK_TIME` (default: 5)
  - `USER_COUNT` (default: 100)
  - `RAMP_UP_PERIOD` (default: 20)

### 2. PerfTestSimulation.java

- Change load: `userJourney(CHAIRS_PERCENTAGE).injectOpen(rampUsers(USER_COUNT).during(RAMP_UP_PERIOD))`
- Pass `CHAIRS_PERCENTAGE` as parameter to `userJourney()`

### 3. UserFlow.java

- Modify `userJourney(Double chairs)` to accept parameter
  - ⚠️ **Important:** The parameter name in the method signature must match the name used in `percent()`. If you name the parameter `chairs`, use `percent(chairs)`. If you name it `chairsProbability`, use `percent(chairsProbability)`.
- Add `.pause(MIN_THINK_TIME, MAX_THINK_TIME)` between steps
- Add conditional logic: `.randomSwitch().on(percent(chairs).then(...))` for chairs steps
- Chairs branch must contain full sequence: navigateToCategory → navigateToProduct → addProductToCart, all with pauses between steps
- Wrap execution inside `.exitBlockOnFail().on(...)` (required syntax)
- All steps must use `group(group).on(...)` for request grouping (required syntax)
- All steps must include at least one `.check(...)` using `regex()` for validation
- **Note:** Your scenario should start with `flushHttpCache()` and `flushCookieJar()` (from Task 3).

## Testing

```bash
# Default parameters
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation

# Custom parameters
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation \
  -DUSER_COUNT=50 -DCHAIRS_PERCENTAGE=30
```

Expected: Gradual ramp-up, only specified percentage add chairs, think time pauses between actions.
