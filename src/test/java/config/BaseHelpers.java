package config;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class BaseHelpers {

    private BaseHelpers() {
    }

    public static final String BASE_URL = System.getProperty("BASE_URL", "http://localhost");

    /**
     * HTTP protocol configuration with realistic headers:
     * - Accept, Accept-Encoding, Accept-Language
     * - Upgrade-Insecure-Requests
     * - User-Agent
     */
    public static final HttpProtocolBuilder HTTP_PROTOCOL = http
            .baseUrl(BASE_URL)
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .upgradeInsecureRequestsHeader("1")
            .userAgentHeader(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            );

    /**
     * Admin AJAX endpoint (e.g., "/wp-admin/admin-ajax.php").
     */
    public static final String ADMIN_AJAX = "/wp-admin/admin-ajax.php";
    public static final String CART_WIDGET_DISABLED = "0";
    public static final String CART_CONTAINER_DISABLED = "0";

    /**
     * They should read values from system properties with default values.
     */
    /** Percentage of users adding chairs (default: 50). */
    public static final double CHAIRS_PERCENTAGE = Double.parseDouble(
            System.getProperty("CHAIRS_PERCENTAGE", "50"));

    /** Percentage of users proceeding to cart (default: 30). */
    public static final double CART_PERCENTAGE = Double.parseDouble(
            System.getProperty("CART_PERCENTAGE", "30"));

    /** Think time between actions in seconds (default: 2). */
    public static final int THINK_TIME = Integer.parseInt(
            System.getProperty("THINK_TIME", "2"));

    /** Standard deviation for Gaussian think-time pauses as a percentage of THINK_TIME (default: 20). */
    public static final double THINK_TIME_STD_DEV_PERCENT = Double.parseDouble(
            System.getProperty("THINK_TIME_STD_DEV_PERCENT", "20"));

    /** Number of virtual users (default: 100). */
    public static final int OPEN_USERS = Integer.parseInt(
            System.getProperty("OPEN_USERS", "100"));

    /** Number of concurrent users (default: 100). */
    public static final int CONCURRENT_USERS =
            Integer.parseInt(System.getProperty("CONCURRENT_USERS", "100"));

    /** Ramp-up period in seconds (default: 20). */
    public static final int RAMP_UP_PERIOD = Integer.parseInt(
            System.getProperty("RAMP_UP_PERIOD", "20"));

    /** Load model: open or closed (default: open). */
    public static final String LOAD_MODEL = System.getProperty("LOAD_MODEL", "open");

    /** Tables quantity in units (default: 1). */
    public static final int TABLES_QUANTITY = Integer.parseInt(
            System.getProperty("TABLES_QUANTITY", "1"));

    /** Chairs quantity in units (default: 1). */
    public static final int CHAIRS_QUANTITY = Integer.parseInt(
            System.getProperty("CHAIRS_QUANTITY", "1"));

    /** Minimum percentage of successful requests (default: 99.0). */
    public static final double SUCCESSFUL_REQUESTS_PERCENT = Double.parseDouble(
            System.getProperty("SUCCESSFUL_REQUESTS_PERCENT", "99.0"));

    /** Home page 95th percentile response time threshold in milliseconds (default: 3000). */
    public static final int HOME_PAGE_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("HOME_PAGE_P95_RESPONSE_TIME_MS", "3000"));

    /** Category page 95th percentile response time threshold in milliseconds (default: 2500). */
    public static final int CATEGORY_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("CATEGORY_P95_RESPONSE_TIME_MS", "2500"));

    /** Tables page 95th percentile response time threshold in milliseconds (default: 3000). */
    public static final int TABLES_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("TABLES_P95_RESPONSE_TIME_MS", "3000"));

    /** Tables add-to-cart 95th percentile response time threshold in milliseconds (default: 4000). */
    public static final int TABLES_ADD_TO_CART_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("TABLES_ADD_TO_CART_P95_RESPONSE_TIME_MS", "4000"));

    /** Chairs page 95th percentile response time threshold in milliseconds (default: 3000). */
    public static final int CHAIRS_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("CHAIRS_P95_RESPONSE_TIME_MS", "3000"));

    /** Chairs add-to-cart 95th percentile response time threshold in milliseconds (default: 4000). */
    public static final int CHAIRS_ADD_TO_CART_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("CHAIRS_ADD_TO_CART_P95_RESPONSE_TIME_MS", "4000"));

    /** Cart page 95th percentile response time threshold in milliseconds (default: 3000). */
    public static final int CART_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("CART_P95_RESPONSE_TIME_MS", "3000"));

    /** Checkout page 95th percentile response time threshold in milliseconds (default: 4000). */
    public static final int CHECKOUT_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("CHECKOUT_P95_RESPONSE_TIME_MS", "4000"));

    /** Choosing country 95th percentile response time threshold in milliseconds (default: 2000). */
    public static final int CHOOSING_COUNTRY_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("CHOOSING_COUNTRY_P95_RESPONSE_TIME_MS", "2000"));

    /** Place order 95th percentile response time threshold in milliseconds (default: 4000). */
    public static final int PLACE_ORDER_P95_RESPONSE_TIME_MS = Integer.parseInt(
            System.getProperty("PLACE_ORDER_P95_RESPONSE_TIME_MS", "4000"));

    public static final String ASSERTION_TYPE = System.getProperty(
            "ASSERTION_TYPE", "smokeTest"
    );
}
