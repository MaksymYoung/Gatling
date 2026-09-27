# Task 3: API Integration and Forms

## Learning Objectives

By completing this task, you will learn:

- How to make POST requests to APIs
- How to send form data in requests
- How to extract data from HTML elements
- How to pass data between requests using session variables

## Overview

In this task, you'll add functionality to view product pages and add products to the shopping cart via API calls. This introduces you to POST requests and form data handling.

## Prerequisites

This task assumes you have completed Task 2. Your code should have `BaseHelpers` with `BASE_URL` and `HTTP_PROTOCOL`, and category navigation should be working.

## Task Requirements

### 1. BaseHelpers

**Location:** `src/test/java/config/BaseHelpers.java`

**Your task:**

- Add a new constant `ADMIN_AJAX` for the WordPress AJAX endpoint: `"/wp-admin/admin-ajax.php"`

**Hints:**

- This endpoint will be used for API calls to add products to cart

### 2. UserFlow

**Location:** `src/test/java/scenarios/UserFlow.java`

**Your task:**

- Implement exact 7-step flow in deterministic order: Home → Tables → Product(Tables) → AddToCart → Chairs → Product(Chairs) → AddToCart
- All steps must execute in order
- Extend your scenario to navigate to product pages
- Add steps to add products to cart for both tables and chairs
- Use the session variables created in Task 2 (like "rndtables" and "rndchairs")

**What you'll learn:**

- How to chain multiple navigation steps
- How to build complete user journeys

**Hints:**

- Add `navigateToProduct()` calls after category navigation
- Add `addProductToCart()` calls after product navigation
- Use descriptive group names like "003*Tables", "004_Tables*", etc.
- Wrap the navigation execution inside `.exitBlockOnFail().on(...)` to ensure proper fail isolation

### 3. Navigation

**Location:** `src/test/java/api/Navigation.java`

**Your task:**

- **New method** `navigateToProduct(String group, String sessionVar)`:
  - Make a GET request to a product page using `#{rnd{sessionVar}}` from session
  - Extract `productId` from the page (look for a shortlink in HTML)
  - Extract `cart_content` from a hidden input field (optional - may not exist)
  - Save both to session variables

**What you'll learn:**

- How to extract data from specific HTML elements
- How to handle optional data extraction
- How to use nested session variable references

**Hints:**

- Use `group(group).on(...)` to wrap your HTTP request (required syntax)
- Build the URL: `BASE_URL + "/products/#{rnd" + sessionVar + "}"`
- Request name must follow format "GET /products/#{rndsessionVar}" or "{METHOD} {ENDPOINT}" (e.g., `http("GET /products/#{rnd" + sessionVar + "}")`)
- Look for `<link rel='shortlink'>` tag to extract productId
- Look for hidden input with name "cart_content"
- Use `.optional()` for fields that might not exist
- Must include at least one `.check(...)` using `regex()` for content validation

- Implement `addProductToCart(String prefix, int quantity)`:
  - Make a POST request to `BASE_URL + ADMIN_AJAX`
  - Send form parameters:
    - `action`: "ic_add_to_cart"
    - `add_cart_data`: build a string using `#{productId}` and `#{cart_content}` from session
    - `cart_widget` and `cart_container`: use your constants
  - Verify the response contains "Added!"
  - Create constants for cart widget and container settings (both set to `"0"`)

**What you'll learn:**

- How to make POST requests
- How to send form data using `formParamMap()`
- How to build dynamic form data using session variables
- How to verify API responses

**Hints:**

- Use `group(prefix + "addToCart").on(...)` for request grouping (required syntax)
- Use `http().post()` for POST requests
- Request name must follow format "POST {ENDPOINT}" (e.g., `http("POST " + ADMIN_AJAX)`)
- Use `formParamMap(Map.of(...))` for grouped form parameters (preferred)
- Build `add_cart_data` string: `"current_product=#{productId}&cart_content=#{cart_content}&current_quantity=" + quantity`
- The `.check(regex("Added!").exists())` verification is mandatory (required)

## Testing Your Implementation

1. **Run the simulation:**

   ```bash
   mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation
   ```

2. **Verify:**
   - The scenario should navigate through: homepage → categories → products → add to cart
   - Check that products are successfully added (look for "Added!" in responses)
   - Verify session variables are being passed correctly between steps

## Key Concepts

### POST Requests

POST requests are used to send data to the server, typically for creating or updating resources.

### Form Data

Form data is sent as key-value pairs, similar to HTML form submissions.

### API Integration

APIs allow you to interact with application functionality without navigating through the UI.

## Common Pitfalls

- **Incorrect form data format:** Make sure the `add_cart_data` string matches the expected format
- **Missing session variables:** Ensure previous steps save the required data
- **Wrong endpoint:** Double-check you're using `ADMIN_AJAX` constant correctly

## Resources

- [Gatling Documentation](https://gatling.io/docs/gatling/)
- [Gatling Scripting: Best Practices](https://kb.epam.com/pages.viewpage.action?spaceKey=EPMPERF&title=Gatling+Scripting%3A+Best+Practices)
- Review the project's `PROJECT_OVERVIEW.md` for context about the overall project structure

---

**Great progress! You're building a complete e-commerce flow!** 🛒
