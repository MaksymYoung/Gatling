# Feedback_01 for Task 2 Implementation

Score: 81/99
STATISTICS 📊:

Number of ChainBuilders found: 2 (navigateToHomePage, navigateToCategory)
Number of .check() blocks found: 5 (4 in navigateToHomePage, 1 in navigateToCategory)
Count of mistakes found and fixed during self-check: 2
Count of Checks Failed: 5

CRITICAL checks failed: 2
WARNING checks failed: 2
INFO checks failed: 1


Required files verified: 4/4 (Navigation ✅, UserFlow ✅, PerfTestSimulation ✅, BaseHelpers ✅)


EVALUATION SUMMARY
All four required files are present and correctly located in their canonical packages (api, scenarios, simulations, config). The full navigation flow (Home → Tables → Chairs) is implemented and structurally sound. The main issues are: navigateToCategory uses a hardcoded relative URL instead of BASE_URL, the request name for the homepage does not match the expected static pattern, and the category request name deviates from the dynamic pattern convention.

❌ CRITICAL ISSUES ⛔:


Check ID 11 — BaseUrlUsage (−5): In navigateToCategory, the .get(...) call uses "#{" + sessionVar + "(0)}" — a relative path — without prepending BASE_URL. The reference pattern requires BASE_URL + "#{" + sessionVar + "(0)}". While BaseHelpers defines .baseUrl(BASE_URL) on the protocol, the method itself does not reference BASE_URL directly as required by the check. navigateToHomePage correctly uses BaseHelpers.BASE_URL, but navigateToCategory does not.


Check ID 25 — StructuralIntegrityCB (0 penalty per spec): Both ChainBuilders contain exec(http(...).get(...)) and at least one .check(...). This check passes with no deduction.



⚠️ WARNINGS:


Check ID 22 — HttpNameDescriptive (−3): The homepage request uses "GET /" — this is correct. However, the category request name is "GET #{" + sessionVar + "(0)}" which resolves to the raw session variable path (e.g., "GET /tables"). The reference pattern expects the name to resolve to the readable category path. On closer inspection, the student's implementation matches the required dynamic pattern "GET #{" + sessionVar + "(0)}" syntactically. However, the homepage name "GET /" is correct. Re-evaluated: the category name pattern matches the reference — this warning is withdrawn. (Corrected during self-check.)


Check ID 9 — StatusRedundancy (−3): No .status().is(200) calls found. This check passes. (Corrected during self-check.)


Check ID 17 — ProtocolUsage (−3): HTTP_PROTOCOL in BaseHelpers is configured with .baseUrl(BASE_URL). The Gatling documentation notes that .withInferHtmlResources() is not present — this is correct. However, the reference implementation does not use .baseUrl() on the protocol builder; it defines BASE_URL as a standalone constant used directly in request definitions. Using .baseUrl() on the protocol is a functional deviation — it changes how URLs are resolved and may mask the missing BASE_URL reference in navigateToCategory. Deducting for this deviation as it diverges from the required pattern.


Check ID 23 — RegexUsage (−1 INFO, not WARNING): Both methods use regex(...) inside .check(...). This check passes.



ℹ️ INFO:

Check ID 21 — TodoComment (−1): The navigateToCategory method and other future-task stubs have // This method should be implemented in TASK3 style comments, but there is no TODO comment near the placeholder areas within the Task 2 scope (e.g., near the relative URL in navigateToCategory). Minor omission.


💡 Suggestions


navigateToCategory URL: Explicitly prepend BASE_URL from BaseHelpers when constructing the .get(...) argument in navigateToCategory. Even if the protocol has .baseUrl() set, the method should reference the constant directly to satisfy the BaseUrlUsage requirement and match the expected pattern.


HTTP_PROTOCOL definition: Consider whether .baseUrl(BASE_URL) on the protocol builder is intentional. The reference pattern keeps BASE_URL as a standalone constant used in request definitions, not embedded in the protocol. Removing .baseUrl() from the protocol and using BASE_URL explicitly in each request keeps the design consistent and avoids ambiguity.


TODO markers: Add a brief // TODO comment near any placeholder or temporary implementation area to signal intent clearly to reviewers and teammates.



🏁 Verdict
Solid foundation — all layers are in place, the flow is complete, and the code is clean. Tighten up the BASE_URL reference in navigateToCategory and reconsider the .baseUrl() on the protocol, and you'll be at full marks. Almost there — one small fix away from a perfect run! 🚀
