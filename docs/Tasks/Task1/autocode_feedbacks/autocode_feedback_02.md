# Feedback_02 for Task 1 Implementation

Score: 96/99
EVALUATION SUMMARY
The implementation is well-structured and covers all three required files with correct package placement and proper Gatling DSL usage. The Navigation.java ChainBuilder is functional, includes all five required .check() calls with regex validation, and is properly wrapped with group(group).on(...). One minor warning was identified regarding the use of .find() instead of .findAll() for extracting navigation links, which deviates from the expected multi-match extraction pattern described in the task requirements.
⚠️ WARNINGS

Check ID 11: All 5 separate .check() method calls are present. However, checks 2–5 use .find() (single match) instead of .findAll() (multiple matches) for extracting navigation links (tables, chairs, cart, checkout). The task explicitly requires .findAll() to extract all matching links and save them as lists into session variables. Using .find() saves only the first match as a string, not a list. Found: .check(regex("href=\"([^\"]*tables[^\"]*)\"").find().saveAs("tables")) — should use .findAll() instead of .find(). (−3 points)

💡 SUGGESTIONS

Review the difference between .find() and .findAll() in Gatling's check API. The task requires saving all matching navigation links as a collection, not just the first occurrence. Using .findAll() ensures the session variable holds a list of all matched values, which is important for subsequent steps in later tasks that may iterate over those links.
The regex patterns used (href="([^"]*tables[^"]*)") are functionally valid and will match the relevant links. Consider whether the pattern is specific enough to avoid false positives on unrelated anchor tags that might also contain the word "tables" or "cart" in their href attributes.
The TODO comment placement and format are correct — well done on following the convention precisely.
All three files are correctly structured, packages are properly named, and the Gatling DSL is used cleanly throughout. The foundation is solid for upcoming tasks.

📊 STATISTICS

Count of mistakes found and fixed during self-check: 1
Count of Checks Failed: 1

CRITICAL checks failed: 0
WARNING checks failed: 1
INFO checks failed: 0


Total checks performed: 17

🏁 VERDICT
✅ PASS — Score 96/99 comfortably exceeds the passing threshold of 90. Excellent foundational work — all three files are correctly implemented, the ChainBuilder is complete with all five validations, grouping is properly applied, and the Gatling DSL is used cleanly throughout. One small adjustment to the extraction method (.find() → .findAll()) would bring this to near-perfect. Great start — you're well-prepared for Task 2!
