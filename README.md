# Automation Framework — AutomationExercise.com

## Tech Stack
- Java 11
- Selenium WebDriver 4.18.1
- TestNG 7.9.0
- Cucumber 7.15.0 (BDD)
- RestAssured 5.4.0 (API Testing)
- Maven (Build Tool)
- Log4j2 (Logging)
- Allure (Reporting)
- WebDriverManager (Auto driver management)
- Docker (Containerized execution)
- GitHub Actions (CI/CD)

---

## Project Structure
```
automation-framework/
├── .github/
│   └── workflows/
│       └── ci.yml                  ← GitHub Actions pipeline
├── src/main/java/com/qa/
│   ├── api/
│   │   ├── ApiClient.java          ← RestAssured base config
│   │   └── ProductApiService.java  ← Product API calls
│   ├── config/
│   │   └── ConfigReader.java       ← Singleton config reader
│   ├── core/
│   │   ├── BaseTest.java           ← TestNG lifecycle
│   │   ├── BasePage.java           ← Shared UI actions
│   │   └── DriverFactory.java      ← ThreadLocal WebDriver
│   ├── models/
│   │   ├── Product.java            ← API POJO
│   │   ├── ProductResponse.java    ← API response wrapper
│   │   ├── Category.java           ← Nested POJO
│   │   └── UserType.java           ← Nested POJO
│   ├── pages/
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── RegisterPage.java
│   │   ├── AccountCreatedPage.java
│   │   ├── ProductsPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutPage.java
│   │   ├── OrderConfirmedPage.java
│   │   └── AccountDeletedPage.java
│   └── utils/
│       ├── WaitUtils.java          ← Explicit waits
│       ├── ScreenshotUtils.java    ← Failure screenshots
│       ├── TestDataGenerator.java  ← Dynamic test data
│       ├── RetryAnalyzer.java      ← Flaky test retry
│       └── RetryListener.java      ← TestNG retry hook
├── src/main/resources/
│   ├── config.properties           ← Environment config
│   └── log4j2.xml                  ← Logging config
├── src/test/java/com/qa/
│   ├── runners/
│   │   └── TestRunner.java         ← Cucumber + TestNG runner
│   └── stepdefs/
│       ├── Hooks.java              ← Before/After + screenshots
│       ├── PurchaseSteps.java      ← UI step definitions
│       └── ApiValidationSteps.java ← API step definitions
├── src/test/resources/
│   └── features/
│       ├── purchase_flow.feature   ← UI scenario
│       └── product_api.feature     ← API scenario
├── Dockerfile                      ← Docker headless execution
├── docker-compose.yml              ← Docker compose config
├── testng.xml                      ← Parallel execution config
├── pom.xml                         ← Maven dependencies
└── README.md
```

---

## Setup Steps

### Prerequisites
- Java 11 installed and JAVA_HOME set
- Maven 3.8+ installed
- Chrome browser installed
- Git installed
- Docker (optional, for containerized execution)

### Clone and install
```bash
git clone <your-repo-url>
cd automation-framework
mvn clean install -DskipTests
```

---

## How to Run Tests

### Run all regression tests locally
```bash
mvn clean test
```

### Run specific tag
```bash
mvn clean test -Dcucumber.filter.tags="@ui"
mvn clean test -Dcucumber.filter.tags="@api"
mvn clean test -Dcucumber.filter.tags="@regression"
```

### Run in headless mode
```bash
mvn clean test -Dheadless=true
```

### Run on specific browser
```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
```

### Combine options
```bash
mvn clean test \
  -Dbrowser=chrome \
  -Dheadless=true \
  -Dcucumber.filter.tags="@regression"
```

### Generate and view Allure report
```bash
# Generate HTML report
mvn allure:report

# Open in browser instantly
mvn allure:serve
```

### Run via Eclipse
Right-click `testng.xml` → Run As → TestNG Suite

---

## Docker Execution

### Run with Docker Compose (recommended)
```bash
# Build image and run tests headless
docker-compose up --build

# View logs in real time
docker-compose logs -f

# Clean up after run
docker-compose down
```

### Run with Docker directly
```bash
# Build image
docker build -t automation-framework .

# Run tests
docker run --rm \
  -v $(pwd)/target:/app/target \
  -v $(pwd)/screenshots:/app/screenshots \
  automation-framework
```

### Access reports after Docker run
Reports are mounted to your local `target/` folder:
```bash
mvn allure:serve
```

---

## CI/CD — GitHub Actions

Pipeline triggers automatically on:
- Every push to `main` branch
- Every pull request targeting `main`
- Manual trigger from GitHub Actions tab

### Manual trigger with custom tags
1. Go to GitHub → Actions tab
2. Select "Automation Tests CI"
3. Click "Run workflow"
4. Enter tag e.g. `@regression` or `@api`
5. Click "Run workflow"

### Pipeline steps
1. Checkout code
2. Setup Java 11
3. Cache Maven dependencies
4. Install Chrome
5. Run tests headless
6. Generate Allure report
7. Upload report as artifact
8. Upload screenshots on failure
9. Upload logs

### Downloading artifacts
After a pipeline run:
1. Go to GitHub → Actions → select run
2. Scroll to Artifacts section
3. Download `allure-report`, `failure-screenshots`
   or `test-logs`

---

## Framework Design

### Architecture
Hybrid framework combining Page Object Model (POM)
for UI layer and BDD (Cucumber) for test definition
layer. Tests are written in plain English Gherkin
readable by non-technical stakeholders.

### Layer breakdown

**Config layer** — `ConfigReader` uses Singleton
pattern to load `config.properties` once from
classpath. Supports CLI overrides via `-D` flags
which take precedence over config file values.
Loads via `getResourceAsStream` so it works
identically locally, in Docker and in CI.

**Core layer** — `DriverFactory` uses
`ThreadLocal<WebDriver>` for thread-safe parallel
execution. Each thread gets its own isolated browser
instance. `driver.remove()` called after each test
to prevent memory leaks in thread pools.

**Page layer** — all pages extend `BasePage` which
provides common actions (click, type, getText) with
built-in explicit waits. Every navigation method
returns the next page object enabling clean method
chaining. No raw WebDriver calls in step definitions.

**API layer** — `ApiClient` provides base RestAssured
configuration. `ProductApiService` fetches and
deserializes product data into POJOs using Jackson.
Same POJO model shared with UI layer for API vs UI
cross-validation in Part 2 of assignment.

**Utils layer** — stateless utility classes with
static methods callable from anywhere without
inheritance. `WaitUtils` used by Hooks and step
definitions that don't extend `BasePage`.

---

## Key Decisions

**ThreadLocal for WebDriver** — ensures each parallel
test thread has its own isolated browser session.
A plain `static WebDriver` field would be shared
across threads causing tests to control each other's
browsers in parallel execution.

**Singleton for ConfigReader** — config file read
from disk exactly once regardless of how many classes
call `getInstance()`. File I/O on every call would
be wasteful and slow.

**`data-qa` attributes for locators** — most stable
selector strategy available on this site. Added
specifically for automation by developers, never used
for styling or JavaScript logic, survives UI redesigns.

**Row-based XPath for cart** — cart product IDs are
dynamically generated by server and non-sequential
(e.g. product-1, product-13). Row position `tr[1]`,
`tr[2]` is always stable regardless of server-assigned
IDs.

**JavaScript click for ad-blocked elements** — site
displays fullscreen ad iframes (`width: 100vw,
height: 100vh`) intercepting all Selenium clicks.
JavaScript `arguments[0].click()` bypasses browser
click interception mechanism entirely.

**JavaScript DOM removal for ads** — rather than
trying to close ads through Shadow DOM, ad elements
are removed from DOM via JavaScript before interactions.
More reliable than locating and clicking a close button
inside a closed Shadow DOM.

**Allure over Extent Reports** — richer reporting
with step-by-step breakdown, timeline view, and
failure categorization. Better demonstrates framework
quality to assignment reviewers.

**Checked exceptions wrapped as RuntimeException** —
keeps step definitions and page classes clean. TestNG
handles test failure reporting naturally without
forcing every caller to handle checked exceptions.

---

## Known Issues

1. **Ad interference** — site displays fullscreen
   Google ad iframes intercepting element clicks.
   Framework handles via JavaScript DOM removal before
   interactions. May still occasionally cause flakiness
   on very slow connections.

2. **CDP version mismatch warning** — Selenium 4.18
   shows a warning about Chrome 146 CDP version.
   Does not affect test execution. Resolves when
   Selenium releases a compatible update.

3. **Parallel thread limit** — running more than 3
   parallel threads against automationexercise.com
   may cause failures due to server-side rate limiting.
   Keep `thread-count` at 3 or below in `testng.xml`.

4. **Cart product IDs are dynamic** — product IDs
   in cart table are server-generated and
   non-sequential. Framework uses row-based XPath
   instead of ID selectors to handle this reliably.

5. **Shadow DOM ads** — ad close button lives inside
   a closed Shadow DOM. Standard Selenium CSS
   selectors cannot access closed shadow roots.
   Handled via JavaScript shadow root piercing.

---

## Bug Reports

### Bug 1 — No feedback when search returns no results
**Severity:** Medium
**Environment:** Chrome 146, automationexercise.com

**Steps to reproduce:**
1. Navigate to Products page
2. Enter search term that matches no products
   e.g. `xyzabc123nothing`
3. Click Search button

**Expected:** A clear message like "No products found
for your search" displayed under "SEARCHED PRODUCTS"

**Actual:** Section appears completely blank with no
message — user has no feedback that the search
returned zero results

**Impact:** Poor user experience — user cannot
distinguish between "still loading" and "genuinely
no results found"

---

### Bug 2 — Payment form accepts completely invalid card details
**Severity:** Critical
**Environment:** Chrome 146, automationexercise.com

**Steps to reproduce:**
1. Complete registration and login
2. Add any product to cart
3. Proceed through checkout
4. On payment page enter:
   - Card number: alphabets e.g. `abcdabcdabcdabcd`
   - CVC: any random value
   - Expiry month: `00`
   - Expiry year: `0000`
5. Click Pay and confirm button

**Expected:**
- Card number field accepts only numeric digits,
  exactly 16 characters
- Expiry month accepts only values between 01-12
- Expiry year accepts only valid future years
- Validation errors shown for all invalid inputs
  before allowing payment submission

**Actual:** Payment processes successfully with
completely invalid card details including alphabets
in card number and impossible expiry date of
month `00` year `0000`

**Impact:** Critical security and data integrity
risk. No payment validation means orders can be
placed without any valid payment information. In
a real production system this would result in
direct financial loss and fraudulent orders.

---

### Bug 3 — Contact Us form submits with only email filled
**Severity:** High
**Environment:** Chrome 146, automationexercise.com

**Steps to reproduce:**
1. Navigate to Contact Us page
2. Fill only the Email field with a valid email
3. Leave Name, Subject and Message completely empty
4. Click Submit

**Expected:** Validation errors displayed for all
empty required fields — Name, Subject and Message
should be mandatory before form submission is allowed

**Actual:** Form submits successfully with only
email entered. Name, Subject and Message fields
have no client-side or server-side validation.

**Impact:** Incomplete and meaningless contact
requests reach the backend. Support team receives
submissions with no actionable information, causing
operational inefficiency and poor data quality.

---

## Manual Testing Scenario

### Scenario — Visual rendering across different screen sizes

**Why automation is not suitable here:**

Verifying the website renders correctly and looks
visually appealing across different devices and
screen sizes (mobile, tablet, desktop) requires
human judgment. While Selenium can resize the
browser window, it cannot determine:

- Whether font sizes feel comfortable to read
- Whether images are proportionally sized and
  not distorted
- Whether spacing and alignment looks visually
  balanced
- Whether colour contrast meets accessibility
  standards (WCAG compliance)
- Whether touch targets are large enough on mobile
  for comfortable interaction
- Whether the overall page feels professional
  and trustworthy to a real user

These are subjective visual quality checks that
require a human eye. Automated visual regression
tools like Percy or Applitools can flag pixel
differences between screenshots but cannot
determine whether the overall user experience
is acceptable — that final judgment always
requires a human reviewer with design sensibility.