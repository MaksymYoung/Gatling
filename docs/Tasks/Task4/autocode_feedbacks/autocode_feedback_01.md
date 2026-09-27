# Feedback_01 for Task 4 Implementation

Score: 82/99
EVALUATION SUMMARY
The submission is structurally sound with all four required packages present and correctly organized. Core functionality including BaseHelpers configuration, Navigation API methods, simulation setup, and randomSwitch branching are all implemented. However, the scenario layer has a structural issue with double-grouping of navigation calls, and there are minor concerns around request naming conventions and code organization.

❌ CRITICAL ISSUES ⛔
Check ID 9 — Every ChainBuilder has group(...) and regex-based .check(...); no .status().is(...)

Reason: In UserFlow.java, the scenario wraps Navigation.navigateToHomePage("001_HomePage") inside an additional group("Home").on(...). Since navigateToHomePage already returns a ChainBuilder wrapped in group(group).on(...) internally, this creates a double-group nesting: group("Home").on(group("001_HomePage").on(...)). The same pattern applies to the Chairs branch where group("Chairs Category").on(Navigation.navigateToCategory(...)) and group("Chair Product").on(Navigation.navigateToProduct(...)) and group("Add Chair To Cart").on(Navigation.addProductToCart(...)) all double-wrap already-grouped ChainBuilders. This is a structural misuse of the grouping pattern — the api methods already handle their own grouping, and the scenario layer should not re-wrap them.
Penalty: −5


⚠️ WARNINGS
Check ID 14 — Scenario inserts .pause(MIN_THINK_TIME, MAX_THINK_TIME) between each major step

Reason: The Chairs branch is missing a pause after addProductToCart("007_Chairs_", QUANTITY). The reference implementation includes a trailing pause after the Chairs addToCart step inside the randomSwitch branch. The current implementation ends the Chairs branch without a final pause.
Penalty: −3

Check ID 24 — Request names descriptive

Reason: In navigateToCategory, the request is named "GET #{" + sessionVar + "(0)}" which resolves at runtime to something like "GET #{tables(0)}" — a session expression, not a resolved path. While this is a common pattern, it produces non-descriptive names in reports (e.g., GET #{tables(0)} rather than GET /tables). Similarly in navigateToProduct, the name "GET /products/#{rndtables}" contains a raw session variable expression. This is a minor but notable naming concern for report readability.
Penalty: −3

Check ID 16 — Chairs branch reuses navigation/cart methods (no duplication/inline HTTP calls)

Reason: The Chairs branch in UserFlow.java wraps already-grouped ChainBuilders in additional group(...).on(...) calls (e.g., group("Chairs Category").on(Navigation.navigateToCategory(...))). While the underlying methods are reused (not duplicated inline), the extra wrapping layer introduces redundant group nesting that deviates from the intended pattern where api methods are called directly without re-wrapping.
Penalty: −3


ℹ️ INFO
Check ID 1 — Package names lowercase, class names PascalCase

Reason: The constant names CART_WIDGET and CART_CONTAINER in BaseHelpers differ from the reference convention of CART_WIDGET_DISABLED and CART_CONTAINER_DISABLED. While the names are still UPPER_SNAKE_CASE and functional, they deviate from the expected naming convention described in the checklist.
Penalty: −1

Check ID 26 — No unresolved TODO placeholders

Reason: BaseHelpers.java contains ASSERTION_TYPE = null with a comment "Replace with actual implementation in TASK6", and AssertionsMap.java and PerformanceAssertions.java contain multiple null placeholders with TODO-equivalent comments. While these are Task 6 stubs and not Task 4 requirements, they represent unresolved placeholder artifacts in the codebase.
Penalty: −1


💡 SUGGESTIONS


Double-grouping in UserFlow: The scenario layer should call api methods directly without wrapping them in additional group(...).on(...) calls. Since each Navigation method already encapsulates its own group, the scenario should simply chain .exec(Navigation.navigateToHomePage("001_HomePage")) without an outer group wrapper. Review how the api layer's group encapsulation is designed to be the single source of grouping.


Trailing pause in Chairs branch: Consider whether a pause after the final step in the Chairs branch is appropriate for realistic user simulation. The reference implementation includes it — think about what a real user would do after adding an item to cart before the scenario ends.


Request naming: Consider whether session-expression-based request names provide sufficient clarity in Gatling reports. Static or partially-static names (e.g., "GET /tables" or "GET /chairs") improve report readability significantly.


Constants naming alignment: Review the naming of CART_WIDGET and CART_CONTAINER against the checklist requirement for CART_WIDGET_DISABLED and CART_CONTAINER_DISABLED. Consistent naming across the codebase improves maintainability.



📊 STATISTICS

Count of mistakes found and fixed during self-check: 2
Count of Checks Failed: 6

CRITICAL checks failed: 1
WARNING checks failed: 3
INFO checks failed: 2


Total checks performed: 27


🏁 VERDICT
✅ PASS
The submission demonstrates solid understanding of Gatling's load configuration, System property parameterization, and conditional branching. The core architecture is correct and all critical components are present. Addressing the double-grouping pattern in the scenario layer and adding the missing trailing pause will bring this solution much closer to the reference implementation. Well done on getting the fundamentals right — the refinements needed are structural polish rather than fundamental gaps.
