# Scenarios and simulations

## 1.1 HTTP Requests

HTTP protocol in Gatling allows you to load test web applications, web services, or websites. It supports HTTP and HTTPS with almost every feature of common browsers such as caching, cookies, and redirect handling.

However, Gatling is not a browser: it does not run JavaScript, does not apply CSS styles, and does not react to UI events. Gatling works at the HTTP protocol level.

HTTP support has a dedicated DSL whose entry point is the http(requestName) method. The request name is important — it acts as a key when computing statistics in reports.

`http("GET_/")
.get("https://demoblaze.com/")`

Request names should follow the format "{METHOD} {ENDPOINT}" (e.g., "GET_/", "POST_/api/cart"). These names appear in Gatling reports and help identify requests during analysis.

Supported HTTP methods: .get, .put, .post, .delete, .patch, .options

### Query Parameters

Frameworks often pass additional information via query parameters — the part of a URL after ?, composed of key=value pairs separated by &.

`http("GET_/prod.html")
.get("https://demoblaze.com/prod.html")
.queryParam("idp_", "1")`

queryParam() adds a single query parameter. Chain multiple calls to add more — Gatling automatically separates them with &:

`http("GET_/prod.html")
.get("https://demoblaze.com/prod.html")
.queryParam("idp_", "1")
.queryParam("category", "phones")`

// Resulting URL: https://demoblaze.com/prod.html?idp_=1&category=phones

### Request Naming Convention

Name every request using the {METHOD} {ENDPOINT} pattern. This is not just a convention — it directly affects the readability of your Gatling HTML report.

Good	                             Avoid
"GET_/"	                             "homepage"
"POST_/wp-admin/admin-ajax.php"	     "add to cart"
"GET #{tables(0)}"	                 "tables page"

For requests that use session variables in the URL, include the variable reference in the name: "GET #{sessionVar(0)}". This makes each request instance distinguishable in the report.

## 1.2 Checks: Validation and Extraction

Checks serve two purposes: validate that a response meets expectations, and extract data from the response to store in the session for later use.

`http("GET_/")
.get("https://demoblaze.com/")
.check(regex("PRODUCT STORE").exists())
.check(regex(""cat":"(.+?)"").findAll().saveAs("categories"))`

Multiple checks can be chained on a single request. Each .check() call performs one independent validation or extraction.

Automatic Status Code Checks
Gatling automatically validates status codes for all 2xx and 304 responses. You only need to write explicit status checks when testing error scenarios (e.g., expecting a 404).

### Check Types

Check -	Description
responseTimeInMillis - Response time in milliseconds
status() - HTTP status code
bodyString, bodyBytes, bodyLength -	Full response body as string, bytes, or length
substring -	Checks for a literal substring in the body
regex -	Applies a Java regex to the body
xpath -	XPath query on XML responses
jsonPath -	JSONPath query on JSON responses
jmesPath -	JMESPath query on JSON responses
css - CSS selector on HTML responses
form -	Captures all inputs from a form tag

### Regex Checks

regex() is the most commonly used check for HTML responses. It applies a Java regular expression pattern to the response body.

`http("GET_/")
.get("https://demoblaze.com/")
.check(regex("<title>(.+?)</title>").is("STORE"))`

To extract all matches as a list:

`http("GET_/")
.get("https://demoblaze.com/")
.check(regex("class="list-group-item">(.+?)</a>").findAll().saveAs("categories"))`

Avoid CSS Checks for Validation
CSS checks require full HTML parsing, which is less CPU-efficient than regex. CSS selectors are also more fragile — they break when HTML structure changes. Use regex() for performance-critical load tests.

### Extraction Methods

Method - Description
find() - First (or only) match — same as find(0)
find(n)	- Match at index n (zero-based)
findAll() -	All matches as a List
findRandom() -	A single random match — simulates realistic user selection
count() - Number of matches as an integer

`.check(jsonPath("$.Items[*].title").find().saveAs("first"))`        // "Samsung galaxy s6"
`.check(jsonPath("$.Items[*].title").findAll().saveAs("all"))`       // List of all titles
`.check(jsonPath("$.Items[*].title").findRandom().saveAs("random"))` // random title
`.check(jsonPath("$.Items[*].title").find(2).saveAs("third")) `      // "Nexus 6"
`.check(jsonPath("$.Items[*].title").count().saveAs("count")) `      // 9


### Validation Methods

Method -	Description
is(value) -	Assert extracted value equals expected
not(value) -	Assert extracted value differs from expected
exists() -	Assert the check matched at least one result
notExists() -	Assert the check matched nothing
in(list) -	Assert value is in the given collection
isNull() / notNull() -	Assert JSON null or non-null
optional() -	Never fail — extract if present, skip if not
The optional() method is particularly useful for extracting values that may or may not be present in a response:

`.check(regex("<input type="hidden" name="cart_content" value='(.*?)'>")
.optional().saveAs("cart_content"))`

### Saving Extracted Values

saveAs("key") stores the extracted value in the virtual user's session under the given key. The stored value can then be referenced in later requests — see Session Variables for EL syntax, list indexing, and debugging.

`http("GET_/")
.get(BASE_URL)
.check(regex(""id":(\d+)").findRandom().saveAs("productId"))`

## 1.3 Scenarios: Definition, exec, Chains

A Gatling test is composed of two main parts: the Scenario (what a user does) and the Injection profile (how many users do it and when). This page covers scenario construction.

### Creating a Scenario

`ScenarioBuilder myScenario = scenario("Browse DemoBlaze")
.exec(flushHttpCache())
.exec(flushCookieJar())
.exec(navigateToHomePage)
.exec(navigateToCategory);`

scenario("name") creates a new ScenarioBuilder; the string is the scenario label in the report
Each .exec() step appends an action or chain to the sequence and executes in order

### Chains: ChainBuilder

A ChainBuilder is a reusable sequence of actions. Combine multiple exec() calls with dot notation:

`public static ChainBuilder browseFlow =
exec(navigateToHomePage)
.pause(1)
.exec(navigateToCategory)
.pause(1)
.exec(navigateToProduct);`

ChainBuilder objects defined as static fields or returned from static methods can be called from any scenario, enabling code reuse.

### Groups

Wrap related requests in a group() to produce composite transaction statistics in the HTML report. Groups appear as named rows alongside individual request rows.

`public static ChainBuilder navigateToHomePage(String group) {
return group(group).on(
exec(http("GET_/")
.get("https://demoblaze.com/")
)
);
}`

Call with a descriptive group name that will appear in reports:

`Navigation.navigateToHomePage("001_HomePage")`

Groups can be nested to model hierarchical user flows.

### Cache and Cookie Initialization

Clear HTTP cache and cookies at the start of each scenario to ensure every virtual user begins with a clean state:

`scenario("E-commerce Journey")
.exec(flushHttpCache())
.exec(flushCookieJar())
.exec(navigateToHomePage)`

Method -	Purpose
flushHttpCache() -	Clears Gatling's in-memory HTTP cache
flushCookieJar() -	Removes all cookies (session and permanent)
flushSessionCookies() - Removes only session cookies (simulates browser close)

### Error Handling: exitBlockOnFail

exitBlockOnFail() stops executing the remaining steps in its block when any step fails. Without it, a failed login would be followed by cart and checkout steps that cannot succeed, polluting the results.

`scenario("E-commerce Journey")
.exec(flushHttpCache())
.exec(flushCookieJar())
.exitBlockOnFail().on(
exec(navigateToHomePage)
.exec(navigateToCategory)
.exec(navigateToProduct)
)`

Use exitBlockOnFail() to wrap any sequence of steps where later steps depend on earlier ones succeeding. Failing fast prevents misleading error counts in the report.

### Static Imports

Gatling's Java API relies heavily on static methods. Add these two imports to every simulation and scenario class:

`import static io.gatling.javaapi.core.CoreDsl.*;`   // scenario(), exec(), group(), pause()
`import static io.gatling.javaapi.http.HttpDsl.*;`   // http(), flushHttpCache(), flushCookieJar()

With static imports you use scenario(), exec(), http() directly without class qualifiers.

### 1.4 Session Variables

Each virtual user in Gatling is backed by a Session — a key-value store that persists for the lifetime of the user's scenario execution. Session variables allow you to capture data from one response and use it in the next request.

#### Storing Values

Use .check(... .saveAs("key")) to store an extracted value:

`http("GET_/")
.get("https://demoblaze.com/")
.check(regex(""cat":"(.+?)"").findAll().saveAs("categories"))`

findAll() stores all matches as a list (see Extraction Methods for the full method reference).

### Using Values in Requests

Reference stored values in URLs, headers, or body using Gatling Expression Language (EL): #{key}.

`http("GET Category")
.get("https://demoblaze.com/#{categories(0)}")`

Access list elements by index: #{key(0)} for the first element, #{key(1)} for the second, and so on.

### Dynamic Navigation

Session variables enable dynamic navigation based on what was extracted from previous pages:

// Extract all category links on homepage
`http("GET_/")
.get(BASE_URL)
.check(regex("href="(/category/.+?)"").findAll().saveAs("categories"))`

// Navigate to the first extracted category
`http("GET #{categories(0)}")
.get(BASE_URL + "#{categories(0)}")`

### Random Selection

Use findRandom() to store a random match, simulating realistic user behavior:

`http("GET Category Page")
.get(BASE_URL + "#{category(0)}")
.check(regex("href="(/products/.+?)"").findRandom().saveAs("randomProduct"))`

The saved value is then used in the next request:

`http("GET #{randomProduct}")
.get(BASE_URL + "#{randomProduct}")`

## 1.5 Simulation: Definition and Structure

A Simulation is the main entry point for a Gatling load test. It wires together the HTTP protocol configuration, one or more scenarios, and the injection profile (how virtual users are started).

### The Simulation Class

A Simulation class must:

Extend Simulation
Call setUp() exactly once, inside an instance initializer block { }

`public class PerfTestSimulation extends Simulation {
    {
        setUp(
            UserFlow.userJourney().injectOpen(atOnceUsers(1))
        );
    }
}`

The instance initializer block { } runs when the class is instantiated by the Gatling test runner.

### setUp() Components

Component -	Purpose
ScenarioBuilder.injectOpen(...) -	Attaches an open-workload injection profile to a scenario
atOnceUsers(n) -	Injects n users simultaneously at test start
.protocols(httpProtocol) -	Applies a shared HTTP protocol to all requests (added in Module 2)
.assertions(...) -  Adds pass/fail performance assertions (added in Module 6)

### Injection Profiles

For Task 1, the simulation uses atOnceUsers(1) — a single virtual user runs the scenario once, letting you verify correctness before applying real load. Full injection profiles (ramp patterns, constant rate, open vs closed workload model) are covered in Module 4 → Load Configuration.

### Running a Simulation

mvn gatling:test -Dgatling.simulationClass=simulations.PerfTestSimulation

Gatling generates an HTML report in target/gatling/<run-folder>/index.html after each run.

# Task 1: Scenarios and Simulations

Create the first working Gatling simulation for the PTE e-commerce application. The simulation navigates to the homepage, validates page content, and extracts navigation links into session variables.

The PTE (Performance Testing Essentials) e-commerce application runs locally via Docker. This task establishes the three-file project skeleton that all subsequent tasks build on: a request class (Navigation), a scenario class (UserFlow), and a simulation class (PerfTestSimulation).

The skeleton files already exist in the project — your job is to fill them in following the specifications below.

## What You Will Build

src/test/java/
├── api/Navigation.java           — GET homepage, validate content, extract links
├── scenarios/UserFlow.java       — Scenario builder with cache flush and exitBlockOnFail
└── simulations/PerfTestSimulation.java  — Simulation with 1 user injection

## Prerequisites

✅ PTE application deployed and running on http://localhost via Docker
✅ Maven project cloned and skeleton files present
✅ mvn gatling:test runs without compilation errors

## Required Static Imports

Add these to every Java file that uses the Gatling DSL:

`import static io.gatling.javaapi.core.CoreDsl.*;`  // scenario(), exec(), group(), exitBlockOnFail()
`import static io.gatling.javaapi.http.HttpDsl.*;`  // http(), flushHttpCache(), flushCookieJar()

## Step-by-Step Guide

### Step 1: Implement PerfTestSimulation.java

The simulation class is the entry point. It wires the scenario to an injection profile.

`
public class PerfTestSimulation extends Simulation {
    {
        setUp(
            UserFlow.userJourney().injectOpen(atOnceUsers(1))
        );
    }
}
`

The class must extend Simulation
setUp() must be called inside the instance initializer block { }
atOnceUsers(1) injects a single user immediately — suitable for verifying the script works before adding load

### Step 2: Implement UserFlow.java

The scenario class defines what each virtual user does.

`
public class UserFlow {
    public static ScenarioBuilder userJourney() {
        return scenario("E-commerce End-to-End Journey")
            .exec(flushHttpCache())
            .exec(flushCookieJar())
            .exitBlockOnFail().on(
                exec(Navigation.navigateToHomePage("001_HomePage"))
            );
    }
}
`

Flush cache and cookies before each run for a clean state
exitBlockOnFail() stops the scenario if the homepage request fails
Pass "001_HomePage" as the group name — it will appear in the HTML report

### Step 3: Implement Navigation.java

The navigation class contains all HTTP request definitions.

`
public class Navigation {
    public static ChainBuilder navigateToHomePage(String group) {
        return group(group).on(
            exec(
                http("GET_/")
                    .get("http://localhost") // TODO: move to BaseHelpers later
                    .check(regex("Performance testing Essentials").exists())
                    .check(regex("href="(/tables[^"]*?)"").find().saveAs("tables"))
                    .check(regex("href="(/chairs[^"]*?)"").find().saveAs("chairs"))
                    .check(regex("href="(/cart[^"]*?)"").find().saveAs("cart"))
                    .check(regex("href="(/checkout[^"]*?)"").find().saveAs("checkout"))
            )
        );
    }
}
`

Check requirements:

1 content validation: regex("Performance testing Essentials").exists()
4 link extractions: tables, chairs, cart, checkout — each saved to a session variable
Total: exactly 5 .check() calls
All wrapped in group(group).on(...) — required for grouped report statistics

## Testing

`mvn gatling:test "-Dgatling.simulationClass=simulations.PerfTestSimulation"`

Expected results:

BUILD SUCCESS
1 request to homepage in the report
Group 001_HomePage visible in the HTML report
Session variables tables, chairs, cart, checkout saved (verify by adding debug print statements if needed)
Report generated in target/gatling/

## Troubleshooting

Issue -	Solution
Check fails: "Performance testing Essentials" not found	- Verify the PTE application is running on http://localhost. Open the URL in a browser to confirm.
Session variable not saved	- Confirm saveAs("tables") is on the correct .check() call. Only checks that pass save their values.
Compilation error on exitBlockOnFail()	- Ensure import static io.gatling.javaapi.core.CoreDsl.* is present.
Report not generated	-  Look for BUILD FAILURE output and fix the reported Java error first.

## Key Learnings

Simulation is the entry point: it wires scenario + injection profile + protocol
flushHttpCache() and flushCookieJar() ensure each virtual user starts clean
group(group).on(...) wraps requests for composite statistics in the report
exitBlockOnFail() prevents cascading failures when a prerequisite step fails
Each .check() call is independent: it either validates or extracts, or both
saveAs("key") stores extracted values for reuse in subsequent requests via #{key}
The http("GET_/") label convention makes the report immediately readable
