# Task 6: Performance Assertions

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

> **📖 Full task description:** For detailed instructions, hints, key concepts explanations, and additional resources, see the `TASK6_README.md` file located at `docs/Tasks/Task6/`.

## Overview

Add performance assertions to validate that your application meets performance requirements.

## Files to Modify/Create

```
src/test/java/
├── config/BaseHelpers.java
├── simulations/PerfTestSimulation.java
├── assertions/PerformanceAssertions.java  (NEW)
└── assertions/AssertionsMap.java         (NEW)
```

## Tasks

### 1. BaseHelpers.java

- Add `ASSERTION_TYPE` (default: "smokeTest")

### 2. PerformanceAssertions.java (NEW)

- Constants: `SMOKE_TEST = "smokeTest"`, `ECOMMERCE_ORDER = "ecommerceOrder"`
- `smokeTestAssertions`: ≥99% successful requests
- `ecommerceOrderAssertions`: 95th percentile response time checks per group

### 3. AssertionsMap.java (NEW)

- Create Map linking assertion types to assertion lists
- Map `SMOKE_TEST` → `smokeTestAssertions`
- Map `ECOMMERCE_ORDER` → `ecommerceOrderAssertions`

### 4. PerfTestSimulation.java

- Add assertions: `ASSERT_MAPPING.get(ASSERTION_TYPE)`

## Testing

```bash
# Default (smoke test)
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation

# E-commerce order assertions
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation \
  -DASSERTION_TYPE=ecommerceOrder
```

Expected: Assertions validate performance, test fails if requirements not met.
