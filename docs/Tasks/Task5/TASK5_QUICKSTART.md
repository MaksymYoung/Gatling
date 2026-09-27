# Task 5: Complex Scenarios and Templates

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

> **📖 Full task description:** For detailed instructions, hints, key concepts explanations, and additional resources, see the `TASK5_README.md` file located at `docs/Tasks/Task5/`.

## Overview

Complete full e-commerce purchase flow: cart → checkout → country selection → order submission.

## Files to Modify/Create

```
src/test/java/
├── config/BaseHelpers.java
├── simulations/PerfTestSimulation.java
├── scenarios/UserFlow.java
└── api/Navigation.java

src/test/resources/
└── bodies/submit.txt (must be updated)
```

## Tasks

### 1. BaseHelpers.java

- Add `CART_PERCENTAGE` constant (default: 30)

### 2. PerfTestSimulation.java

- Update `userJourney()` to pass `CHAIRS_PERCENTAGE` and `CART_PERCENTAGE`

### 3. UserFlow.java

- Modify `userJourney(Double chairs, Double cart)` to accept two parameters
- Add conditional checkout flow:
  - Navigate to cart
  - Navigate to checkout
  - Choose country
  - Submit order
- Use `.randomSwitch().on(percent(cart).then(...))` for checkout steps
- Cart branch must contain full sequence: navigateToCart → navigateToCheckout → chooseCountry → placeOrder, all with pauses between steps
- Wrap execution inside `.exitBlockOnFail().on(...)` (required syntax)
- All steps must use `group(group).on(...)` for request grouping (required syntax)
- All steps must include at least one `.check(...)` using `regex()` for validation
- Add pauses inside the Cart branch between each step (navigateToCart, navigateToCheckout, chooseCountry, placeOrder)

### 4. Navigation.java

- **New method** `navigateToCart(String group, String sessionVar)`:
  - GET request to cart page using session variable
  - Request name must follow format "GET #{sessionVar(0)}" or "{METHOD} {ENDPOINT}" (e.g., `http("GET #{" + sessionVar + "(0)}")`)
  - Extract `cart_content` from hidden input (optional)
- **New method** `navigateToCheckout(String group, String sessionVar)`:
  - GET request to checkout page
  - Request name must follow format "GET #{sessionVar(0)}" or "{METHOD} {ENDPOINT}" (e.g., `http("GET #{" + sessionVar + "(0)}")`)
  - Extract: `cart_content`, product prices (multiple), `trans_id`, `total_net`, random `country_code`
  - Process session: build `products_price` string from extracted prices
  - Process the products list in a session function to build products_price string
  - Handle both single and multiple products (check list size)
- **New method** `chooseCountry(String group)`:
  - **Step 1** - HTTP POST request:
    - POST to `ADMIN_AJAX` endpoint
    - Request name must follow format "POST {ENDPOINT}" (e.g., `http("POST " + ADMIN_AJAX)`)
    - Send form parameters: `action="ic_state_dropdown"`, `country_code="#{country_code}"` (from session), `state_code=""` (empty string)
    - Extract random `state_code` from JSON response using regex `"value":"([A-Z0-9_]+)"` with `.optional()` (some countries don't have states)
  - **Step 2** - Session validation:
    - Add second `exec` block with session function
    - Check if `state_code` exists in session (was extracted in Step 1)
    - If `state_code` is missing (extraction failed), set it to empty string `""`
    - This guarantees `state_code` is always available for the next step (`placeOrder`)
- **New method** `placeOrder(String group, String sessionVar)`:
  - POST request to checkout page (URL from session variable `sessionVar`)
  - Request name must follow format "POST #{sessionVar(0)}" or "{METHOD} {ENDPOINT}" (e.g., `http("POST #{" + sessionVar + "(0)}")`)
  - Use multipart/form-data format:
    - Set header: `content-type: multipart/form-data`
    - Chain: `.asMultipartForm().asFormUrlEncoded().bodyPart(ElFileBodyPart("bodies/submit.txt"))`
  - Use file template `bodies/submit.txt` for request body (located at `src/test/resources/bodies/submit.txt`)
  - The template will use session variables: `#{cart_content}`, `#{products_price}`, `#{total_net}`, `#{trans_id}`, `#{country_code}`, `#{state_code}`
  - Must include `.check(regex("Thank You").exists())` to verify successful order submission (required)

### 5. Update submit.txt template

**Location:** `src/test/resources/bodies/submit.txt`

**Your task:**

- Update the file template with Gatling EL placeholders
- Required placeholders: `#{cart_content}`, `#{products_price}`, `#{total_net}`, `#{trans_id}`, `#{country_code}`, `#{state_code}`
- This file is **mandatory** for order submission

## Testing

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
```

Expected: Only specified percentage complete checkout, "Thank You" in response, all session variables passed correctly.
