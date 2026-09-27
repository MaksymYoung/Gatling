# Task 5: Complex Scenarios and Templates

## Learning Objectives

By completing this task, you will learn:

- How to extract complex data from HTML responses
- How to process and transform session data
- How to use file templates for request bodies
- How to make multipart/form-data requests
- How to complete a full end-to-end user journey

## Overview

In this task, you'll complete the full e-commerce purchase flow: cart → checkout → country selection → order submission. You'll learn advanced techniques for handling complex data and using file templates.

## Prerequisites

This task assumes you have completed Task 4. Your scenario should have parameterized load, think time, and conditional logic for chairs. Note: Performance assertions are added in Task 6.

## Task Requirements

### 1. BaseHelpers

**Location:** `src/test/java/config/BaseHelpers.java`

**Your task:**

- Add `CART_PERCENTAGE` constant (default: 30) - percentage of users who proceed to checkout

**Hints:**

- Follow the same pattern as `CHAIRS_PERCENTAGE`

### 2. PerfTestSimulation

**Location:** `src/test/java/simulations/PerfTestSimulation.java`

**Your task:**

- Update `userJourney()` call to pass both `CHAIRS_PERCENTAGE` and `CART_PERCENTAGE`

### 3. UserFlow

**Location:** `src/test/java/scenarios/UserFlow.java`

**Your task:**

- Modify `userJourney()` to accept two parameters: `Double chairs, Double cart`
- Add a second conditional block for checkout flow:
  - Navigate to cart
  - Navigate to checkout
  - Choose country
  - Submit order
- Cart branch must contain full sequence: navigateToCart → navigateToCheckout → chooseCountry → placeOrder, all with pauses between steps
- Only the specified percentage of users should complete checkout

**What you'll learn:**

- How to chain multiple conditional blocks
- How to model complete user journeys with different paths

**Hints:**

- Add another `.randomSwitch().on(percent(cart).then(...))` block
- Include think time pauses between checkout steps
- Use descriptive group names: "008_Cart", "009_Checkout", etc.
- Wrap the navigation execution inside `.exitBlockOnFail().on(...)` to ensure proper fail isolation
- All steps must use `group(group).on(...)` for request grouping (required syntax)
- All steps must include at least one `.check(...)` using `regex()` for validation
- Add pauses inside the Cart branch between each step (navigateToCart, navigateToCheckout, chooseCountry, placeOrder)

### 4. Navigation

**Location:** `src/test/java/api/Navigation.java`

**Your task:**

- **New method** `navigateToCart(String group, String sessionVar)`:
  - GET request to cart page using session variable
  - Request name must follow format "GET #{sessionVar(0)}" or "{METHOD} {ENDPOINT}" (e.g., `http("GET #{" + sessionVar + "(0)}")`)
  - Extract `cart_content` from hidden input (optional)
- **New method** `navigateToCheckout(String group, String sessionVar)`:
  - GET request to checkout page
  - Request name must follow format "GET #{sessionVar(0)}" or "{METHOD} {ENDPOINT}" (e.g., `http("GET #{" + sessionVar + "(0)}")`)
  - Extract multiple pieces of data:
    - `cart_content` from hidden input
    - Product prices (multiple matches)
    - `trans_id` from hidden input
    - `total_net` from input
    - Random `country_code` from dropdown options
  - Process session: build `products_price` string from extracted product prices
  - Handle both single and multiple products

**What you'll learn:**

- How to extract multiple data points from one response
- How to process and transform session data
- How to handle lists and build strings from them

**Hints:**

- Use `.findAll()` to get multiple matches
- Use `.captureGroups()` to extract specific parts of regex
- Use `.findRandom()` for dropdown selection
- Process the products list in a session function to build the price string
- Check if list size > 1 to handle multiple products

### 5. Navigation.chooseCountry()

**Location:** `src/test/java/api/Navigation.java`

**Your task:**

- Implement `chooseCountry(String group)` with **two exec blocks**:
  - **First exec block** - HTTP POST request:
    - POST to `ADMIN_AJAX` endpoint
    - Request name must follow format "POST {ENDPOINT}" (e.g., `http("POST " + ADMIN_AJAX)`)
    - Send form parameters: `action="ic_state_dropdown"`, `country_code="#{country_code}"` (from session), `state_code=""` (empty string)
    - Extract random `state_code` from JSON response using regex `"value":"([A-Z0-9_]+)"` with `.optional()` (some countries don't have states)
  - **Second exec block** - Session validation:
    - Add session function to check if `state_code` exists in session (was extracted in first block)
    - If `state_code` is missing (extraction failed), set it to empty string `""`
    - This guarantees `state_code` is always available for the next step (`placeOrder`)

**What you'll learn:**

- How to make API calls that depend on previous responses
- How to handle optional data extraction
- How to ensure session variables are always set (even when extraction fails)

**Hints:**

- Use form parameters similar to cart API
- Use `.optional()` for state_code extraction in the first exec block
- Add a second exec block with `exec(session -> { ... })` to validate and set default value

### 6. Navigation.placeOrder()

**Location:** `src/test/java/api/Navigation.java`

**Your task:**

- Implement `placeOrder(String group, String sessionVar)`:
  - POST request to checkout page (URL from session variable `sessionVar`)
  - Request name must follow format "POST #{sessionVar(0)}" or "{METHOD} {ENDPOINT}" (e.g., `http("POST #{" + sessionVar + "(0)}")`)
  - Use multipart/form-data format:
    - Set header: `content-type: multipart/form-data`
    - Chain: `.asMultipartForm().asFormUrlEncoded().bodyPart(ElFileBodyPart("bodies/submit.txt"))`
  - Use file template `bodies/submit.txt` for request body (located at `src/test/resources/bodies/submit.txt`)
  - The template will use session variables: `#{cart_content}`, `#{products_price}`, `#{total_net}`, `#{trans_id}`, `#{country_code}`, `#{state_code}`
  - Must include `.check(regex("Thank You").exists())` to verify successful order submission (required)

**What you'll learn:**

- How to use file templates for complex request bodies
- How to make multipart/form-data requests
- How to use session variables in file templates

**Hints:**

- Use `ElFileBodyPart("bodies/submit.txt")` for file template
- Set content-type header to "multipart/form-data"
- Use `.asMultipartForm()` and `.asFormUrlEncoded()` chain
- The template file uses Gatling EL placeholders like `#{cart_content}`, `#{products_price}`, etc.
- Note: `state_code` should already be set by `chooseCountry()` method, so no additional validation needed here
- The multipart POST with thank-you regex validation is mandatory (required)

### 7. Update submit.txt template

**Location:** `src/test/resources/bodies/submit.txt`

**Your task:**

- Update the file template with Gatling EL placeholders
- **This file is mandatory** for order submission
- Required placeholders: `#{cart_content}`, `#{products_price}`, `#{total_net}`, `#{trans_id}`, `#{country_code}`, `#{state_code}`

**What you'll learn:**

- How to structure file templates for complex request bodies
- How to use Gatling EL syntax in templates

**Hints:**

- The file should contain form data with placeholders
- Placeholders are replaced with session variable values at runtime
- Use the format: `key=#{variableName}&key2=#{variableName2}`

## Testing Your Implementation

1. **Run the simulation:**

   ```bash
   mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
   ```

2. **Verify:**
   - Only specified percentage of users should complete checkout
   - Order submission should show "Thank You" in response
   - All session variables should be correctly passed through the flow

## Key Concepts

### Complex Data Extraction

Real applications often require extracting and processing multiple data points from responses.

### File Templates

Templates allow you to reuse complex request bodies and make them easier to maintain.

### Session Processing

Sometimes you need to transform session data before using it in requests.

### End-to-End Flows

Complete user journeys test the entire application workflow, not just individual pages.

## Common Pitfalls

- **Missing session variables:** Ensure all required data is extracted in previous steps
- **Incorrect template syntax:** File templates use `#{variableName}` syntax
- **List processing:** Remember to handle both single and multiple items in lists
- **Optional fields:** Use `.optional()` for data that might not exist

## Resources

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Scripting: Best Practices](https://kb.epam.com/pages.viewpage.action?spaceKey=EPMPERF&title=Gatling+Scripting%3A+Best+Practices)
- Review the project's `PROJECT_OVERVIEW.md` for context about the overall project structure

---

**Congratulations! You've built a complete e-commerce load test!** 🎉
