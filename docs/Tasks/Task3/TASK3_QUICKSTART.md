# Task 3: API Integration and Forms

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

> **📖 Full task description:** For detailed instructions, hints, key concepts explanations, and additional resources, see the `TASK3_README.md` file located at `docs/Tasks/Task3/`.

## Overview

Add product page navigation and cart functionality via API calls using POST requests and form data.

## Files to Modify/Create

```
src/test/java/
├── config/BaseHelpers.java
├── scenarios/UserFlow.java
└── api/Navigation.java
```

## Tasks

### 1. BaseHelpers.java

- Add constant `ADMIN_AJAX = "/wp-admin/admin-ajax.php"`

### 2. UserFlow.java

- Implement exact 7-step flow in deterministic order: Home → Tables → Product(Tables) → AddToCart → Chairs → Product(Chairs) → AddToCart
- All steps must execute in order
- Wrap execution inside `.exitBlockOnFail().on(...)` (required syntax)
- Add product navigation steps: `navigateToProduct()` for tables and chairs
- Add cart steps: `addProductToCart()` after each product navigation
- Use session variables: `"rndtables"`, `"rndchairs"`

### 3. Navigation.java

- **New method** `navigateToProduct(String group, String sessionVar)`:
  - Use `group(group).on(...)` for request grouping (required syntax)
  - GET request to `BASE_URL + "/products/#{rnd" + sessionVar + "}"`
  - Extract `productId` from `<link rel='shortlink'>` tag
  - Extract `cart_content` from hidden input (optional)
  - Save both to session variables
  - Must include at least one `.check(...)` using `regex()` for validation
- **New method** `addProductToCart(String prefix, int quantity)`:

  - Constants: `CART_WIDGET = "0"`, `CART_CONTAINER = "0"`
  - POST request to `BASE_URL + ADMIN_AJAX`
  - Request name must follow format "POST {ENDPOINT}" (e.g., `http("POST " + ADMIN_AJAX)`)
  - Form parameters:
    - `action`: "ic_add_to_cart"
    - `add_cart_data`: `"current_product=#{productId}&cart_content=#{cart_content}&current_quantity=" + quantity`
    - `cart_widget`, `cart_container`: use constants
  - Use `formParamMap(Map.of(...))` for grouped form parameters (preferred)
  - Must include `.check(regex("Added!").exists())` to verify success (required)

## Testing

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
```

Expected: Flow through homepage → categories → products → add to cart, "Added!" in responses.
