# Feedback_02 for Task 3 Implementation

Score: 84/99
EVALUATION SUMMARY
The implementation is well-structured with all required layers correctly placed (api, scenarios, simulations, config). The 7-step flow is present and deterministic. The core API methods exist with proper grouping, POST endpoint usage, formParamMap, and success regex check. A few issues remain: the productId extraction regex is non-standard and may fail on actual HTML, CART_WIDGET/CART_CONTAINER are defined as local (non-class-level) constants, and the group name prefixes for AddToCart steps lack category context.

❌ CRITICAL ISSUES ⛔

Check ID 30 — ProductDataExtraction: The navigateToProduct() method extracts productId using regex("<link rel=['\"]shortlink['\"] href=['\"][^'\"]*\\?p=(\\d+)['\"]\\s*/?>"). The reference pattern targets <link rel='shortlink' href='BASE_URL/\\?p=(\\d+)' /> with single quotes and the BASE_URL anchor. The student's pattern uses a character class ['\"] for quotes and omits the BASE_URL anchor, making it looser and potentially matching unintended shortlink tags. The cart_content extraction uses regex("<input[^>]*name=['\"]cart_content['\"][^>]*value=['\"]([^'\"]*)['\"]") which differs from the reference <input hidden type=\"hidden\" name=\"cart_content\" value='(.*?)'> — notably the reference uses single quotes for the value attribute, while the student's pattern uses a character class. These patterns are functionally risky and may not reliably extract the required session data for the POST request. −5 points


⚠️ WARNINGS


Check ID 32 — GroupUsage (descriptive prefixes): The addProductToCart() calls in the scenario use prefixes "003_Tables_" and "005_Chairs_", producing group names "003_Tables_addToCart" and "005_Chairs_addToCart". However, the navigateToProduct() calls use group names "003_Product" and "005_Product" — these group names do not include the category (Tables/Chairs), making reports less informative for the product navigation steps. The AddToCart prefixes themselves are acceptable, but the product navigation group names lack category context. −3 points


Check ID 31 — SessionDataPreparation: The navigateToProduct() method includes a session manipulation exec(session -> {...}) block that normalizes the product path. While the intent is reasonable, this block runs before the HTTP request and modifies rndVar. If the path normalization produces an incorrect value (e.g., double slashes or wrong format), the subsequent GET request and regex extractions will fail, leaving productId and cart_content unpopulated in the session before the POST. The fragile nature of this pre-processing step introduces a risk to correct session data preparation. −3 points


Check ID 21 — HttpNameDescriptive: The HTTP request name for navigateToProduct() is "GET /products/#{" + rndVar + "}" which resolves to "GET /products/#{rndtables}" — this is a session variable reference, not the resolved path. The reference uses "GET /products/#{rnd" + sessionVar + "}" which produces the same pattern. This is acceptable per the reference, so no deduction here. However, the group name "003_Product" and "005_Product" for navigateToProduct() calls are not descriptive enough to distinguish Tables from Chairs in reports. This overlaps with Check ID 32 above — no additional deduction applied here.



ℹ️ INFO


Check ID 33 — ConstantsUsage: CART_WIDGET and CART_CONTAINER are defined as private static final class-level constants in Navigation.java — this is correct and reusable. No deduction for magic literals in API payloads. However, they are defined in Navigation.java rather than in BaseHelpers.java alongside ADMIN_AJAX. This is a minor organizational note, not a violation. No deduction.


Check ID 20 — TodoComment: The methods navigateToCart, navigateToCheckout, chooseCountry, and placeOrder have // This method should be implemented in TASK5 comments, which is appropriate. No TODO markers are present near the regex patterns in navigateToProduct() where the implementation deviates from the reference. −1 point



💡 SUGGESTIONS


Tighten the productId extraction regex in navigateToProduct() — anchor it to BASE_URL and use the exact quote style present in the actual HTML (' single quotes for the shortlink href). This ensures the pattern matches only the intended tag and captures a clean numeric ID.


Tighten the cart_content extraction regex — the actual HTML uses single quotes for the value attribute (value='...'). Using a character class ['\"] is more permissive than needed and may cause unexpected matches. Align the pattern with the actual HTML structure.


Consider removing or simplifying the session manipulation exec(session -> {...}) block in navigateToProduct(). If navigateToCategory() already saves the path in the correct format (starting with /), the normalization step is unnecessary and adds complexity. Verify the format saved by navigateToCategory() and remove the pre-processing if it is redundant.


Use more descriptive group names for navigateToProduct() calls — include the category name (e.g., "003_Tables", "006_Chairs") so Gatling reports clearly identify which product navigation step belongs to which category.


The navigateToCategory() regex href=\"(?:https?://[^/]+)?(/products/[^\"]+)\" does not anchor to BASE_URL. Consider aligning it with the reference pattern BASE_URL + "/products/(.+?)\">" to ensure only product URLs from the target host are extracted.



📊 STATISTICS

Number of ChainBuilders found: 4 (in api package — navigateToHomePage, navigateToCategory, navigateToProduct, addProductToCart)
Number of .check() blocks found: 8 (across all active api methods)
Count of mistakes found and fixed during self-check: 2 (re-evaluated Check ID 33 — constants are class-level static final, not local variables, so Warning −3 was removed; re-evaluated Check ID 21 to avoid double-counting with ID 32)
Count of Checks Failed: 4

CRITICAL checks failed: 1 (ID 30)
WARNING checks failed: 2 (ID 32, ID 31)
INFO checks failed: 1 (ID 20)


Total checks performed: 36

Penalty calculation: −5 (ID 30) −3 (ID 32) −3 (ID 31) −1 (ID 20) = −12 points
Final Score: 99 − 15 = 84/99

🏁 VERDICT
✅ PASS — Score 84/99 meets the passing threshold of 80.
Solid implementation with all structural layers correctly placed, a complete deterministic 7-step flow, proper POST endpoint usage, formParamMap, and success regex validation. The main areas to improve are the regex patterns in navigateToProduct() (tighten them to match the actual HTML structure) and the group naming for product navigation steps. Address these and you'll be very close to a perfect score!
